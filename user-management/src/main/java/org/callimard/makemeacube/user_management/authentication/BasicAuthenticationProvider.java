package org.callimard.makemeacube.user_management.authentication;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.callimard.makemeacube.models.sql.User;
import org.callimard.makemeacube.models.sql.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Component
public class BasicAuthenticationProvider implements AuthenticationProvider {

    // Variables.

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    // Methods.

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = authentication.getName();
        var password = (String) authentication.getCredentials();

        User user = searchUser(username);

        if (checkPassword(password, user)) {
            return UsernamePasswordAuthenticationToken.authenticated(user, password, Lists.newArrayList());
        } else {
            throw new BadCredentialsException("Unknown username or password");
        }
    }

    private User searchUser(String username) {
        var user = userRepository.findByMail(username.toLowerCase(Locale.FRENCH));

        if (user.isEmpty()) {
            user = userRepository.findByPseudo(username.toLowerCase(Locale.FRENCH));
        }

        // Unknown mail or pseudo
        if (user.isEmpty()) {
            throw new BadCredentialsException("Unknown username or password");
        }

        return user.get();
    }

    private boolean checkPassword(String password, User user) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
