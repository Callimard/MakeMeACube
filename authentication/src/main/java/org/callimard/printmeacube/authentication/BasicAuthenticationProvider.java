package org.callimard.printmeacube.authentication;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.callimard.printmeacube.entities.sql.User;
import org.callimard.printmeacube.entities.sql.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

        log.info("Try basic authentication for {}", username);

        User user = searchUser(username);

        if (checkPassword(password, user)) {
            return UsernamePasswordAuthenticationToken.authenticated(user, password, Lists.newArrayList());
        } else {
            log.warn("Fail basic authentication for {} (wrong password)", username);
            throw new BadCredentialsException("Unknown username or password");
        }
    }

    private User searchUser(String username) {
        // Search by mail
        var user = userRepository.findByMail(username);

        if (user.isEmpty()) {
            // Search by pseudo
            user = userRepository.findByPseudo(username);
        }

        // Unknown mail or pseudo
        if (user.isEmpty()) {
            log.warn("Fail basic authentication for {} (unknown mail or pseudo)", username);
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
