package org.callimard.printmeacube.user_management.registration;

import org.callimard.printmeacube.common.RegistrationProvider;
import org.callimard.printmeacube.common.validation.ValidEmail;
import org.callimard.printmeacube.common.validation.ValidPassword;
import org.callimard.printmeacube.entities.sql.User;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

public interface UserRegistrationService {

    record BasicUserRegistrationDTO(@ValidEmail @Size(max = 255) String mail,
                                    @NotNull @NotBlank @Size(min = 5, max = 255) String pseudo,
                                    @ValidPassword @Size(max = 30) String password) {
        public User generateUserWith(RegistrationProvider provider, Instant creationDate) {
            return new User(mail, pseudo, password, provider, creationDate);
        }
    }

    /**
     * Try to register the user and send an email to verify the specified email.
     *
     * @param basicUserRegistrationDTO the dto which contains user registration information
     * @param provider                 the registration provider
     *
     * @return the user registered
     */
    User basicUserRegistration(@Valid BasicUserRegistrationDTO basicUserRegistrationDTO, @NotNull RegistrationProvider provider);
}
