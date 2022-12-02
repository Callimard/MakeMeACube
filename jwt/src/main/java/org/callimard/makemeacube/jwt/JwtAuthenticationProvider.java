package org.callimard.makemeacube.jwt;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    // Variables.

    private final JWTVerifier jwtVerifier;

    private final ObjectMapper mapper;

    // Methods.

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BearerTokenAuthenticationToken bearerAuth = (BearerTokenAuthenticationToken) authentication;

        try {
            var jwt = jwtVerifier.verify(bearerAuth.getToken());
            var jwtAccount = JwtAccount.extractAccountFrom(mapper, jwt);
            var auth = new JwtAccountAuthentication(jwtAccount, jwt.getToken());
            auth.setDetails(bearerAuth.getDetails());

            return auth;
        } catch (JWTVerificationException | JsonProcessingException e) {
            throw new BadCredentialsException("Jwt is not correct", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(BearerTokenAuthenticationToken.class);
    }
}
