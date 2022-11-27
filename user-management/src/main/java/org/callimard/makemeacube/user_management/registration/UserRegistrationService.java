package org.callimard.makemeacube.user_management.registration;

import org.callimard.makemeacube.common.RegistrationProvider;
import org.callimard.makemeacube.common.validation.ValidEmail;
import org.callimard.makemeacube.common.validation.ValidPassword;
import org.callimard.makemeacube.common.validation.ValidPhone;
import org.callimard.makemeacube.entities.sql.User;
import org.callimard.makemeacube.entities.sql.UserAddress;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface UserRegistrationService {

    record BasicUserRegistrationDTO(@ValidEmail @Size(max = 255) String mail,
                                    @NotNull @NotBlank @Size(min = 5, max = 255) String pseudo,
                                    @ValidPassword @Size(max = 30) String password) {
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

    record AddressInformationDTO(@NotNull @NotBlank String address,
                                 @NotNull @NotBlank String city,
                                 @NotNull @NotBlank String country,
                                 @NotNull @NotBlank String postalCode) {

        public UserAddress generateUserAddress(User user) {
            return new UserAddress(-1, address, city, country, postalCode, user);
        }
    }

    record MakerUserRegistrationDTO(@ValidEmail @Size(max = 255) String mail,
                                    @NotNull @NotBlank @Size(min = 5, max = 255) String pseudo,
                                    @ValidPassword @Size(max = 30) String password,
                                    @NotNull @NotBlank String firstName,
                                    @NotNull @NotBlank String lastName,
                                    @NotNull @Valid AddressInformationDTO address,
                                    @NotNull @ValidPhone String phone,
                                    @NotNull @NotBlank String makerDescription) {
    }

    User makerUserRegistration(@Valid MakerUserRegistrationDTO makerUserRegistrationDTO, @NotNull RegistrationProvider provider);
}
