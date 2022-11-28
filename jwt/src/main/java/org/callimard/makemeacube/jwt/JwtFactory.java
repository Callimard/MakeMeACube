package org.callimard.makemeacube.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.callimard.makemeacube.models.sql.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class JwtFactory {

    // Variables.

    private final JwtConfiguration jwtConfiguration;

    private final Algorithm hmac256;

    private final ObjectMapper mapper;

    // Methods.

    public String generateJwtFor(Instant now, User user) throws JsonProcessingException {
        var jwtAccount = generateJwtAccount(user);
        return generateJwtFor(now, jwtAccount);
    }

    public String generateJwtFor(Instant now, JwtAccount jwtAccount) throws JsonProcessingException {
        return JWT.create()
                .withIssuedAt(now)
                .withExpiresAt(now.plus(jwtConfiguration.getDefaultTimeExpiration(), ChronoUnit.MINUTES))
                .withJWTId(UUID.randomUUID().toString())
                .withClaim(JwtAccount.CLAIM_ACCOUNT, mapper.writeValueAsString(jwtAccount))
                .sign(hmac256);
    }

    private JwtAccount generateJwtAccount(User user) {
        return new JwtAccount(user.getMail(), user.getPseudo(), user.getFirstName(), user.getLastName(), Lists.newArrayList());
    }
}
