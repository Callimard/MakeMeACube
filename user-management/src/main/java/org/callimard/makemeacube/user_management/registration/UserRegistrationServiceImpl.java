package org.callimard.makemeacube.user_management.registration;

import lombok.RequiredArgsConstructor;
import org.callimard.makemeacube.common.RegistrationProvider;
import org.callimard.makemeacube.entities.sql.User;
import org.callimard.makemeacube.entities.sql.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@RequiredArgsConstructor
@Validated
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    // Variables.

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;


    // Methods.

    @Override
    public User basicUserRegistration(@Valid BasicUserRegistrationDTO basicUserRegistrationDTO, @NotNull RegistrationProvider provider) {
        // TODO Manage the email verification
        var user = new User(basicUserRegistrationDTO.mail(), basicUserRegistrationDTO.pseudo(),
                            passwordEncoder.encode(basicUserRegistrationDTO.password()), provider, Instant.now());
        return userRepository.save(user);
    }
}
