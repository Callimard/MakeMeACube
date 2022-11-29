package org.callimard.makemeacube.user_management.registration;

import org.callimard.makemeacube.common.validation.ValidEmail;
import org.callimard.makemeacube.common.validation.ValidPassword;
import org.callimard.makemeacube.common.validation.ValidPhone;
import org.callimard.makemeacube.models.aop.UserAddressId;
import org.callimard.makemeacube.models.aop.UserId;
import org.callimard.makemeacube.models.sql.RegistrationProvider;
import org.callimard.makemeacube.models.sql.User;
import org.callimard.makemeacube.models.sql.UserAddress;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface UserRegistrationService {

    record BasicUserRegistrationDTO(@ValidEmail @Size(max = 255) String mail, @NotNull @NotBlank @Size(min = 5, max = 255) String pseudo,
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
    User basicUserRegistration(@NotNull @Valid BasicUserRegistrationDTO basicUserRegistrationDTO, @NotNull RegistrationProvider provider);

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
                                    @NotNull @NotBlank @Size(min = 1, max = 255) String firstName,
                                    @NotNull @NotBlank @Size(min = 1, max = 255) String lastName,
                                    @NotNull @Valid AddressInformationDTO address,
                                    @NotNull @ValidPhone String phone,
                                    @NotNull @NotBlank String makerDescription) {
    }

    User makerUserRegistration(@NotNull @Valid MakerUserRegistrationDTO makerUserRegistrationDTO, @NotNull RegistrationProvider provider);

    record UserUpdatedInformation(@NotNull @NotBlank @Size(min = 5, max = 255) String pseudo,
                                  @Size(max = 255) String firstName,
                                  @Size(max = 255) String lastName,
                                  @NotNull @ValidPhone String phone,
                                  String makerDescription,
                                  @NotNull Boolean isMaker) {

        public User updatedUser(User user) {
            var updatedUser = new User(user);
            updatedUser.setPseudo(pseudo);
            updatedUser.setFirstName(firstName);
            updatedUser.setLastName(lastName);
            updatedUser.setPhone(phone);
            updatedUser.setMakerDescription(makerDescription);
            updatedUser.setIsMaker(isMaker);
            return updatedUser;
        }

    }

    User updateUserInformation(@NotNull @UserId Integer userId, @NotNull @Valid UserUpdatedInformation userUpdatedInformation);

    User addUserAddress(@NotNull @UserId Integer userId, @NotNull @Valid AddressInformationDTO addressInformationDTO);

    User deleteUserAddress(@NotNull @UserId Integer userId, @NotNull @UserAddressId Integer userAddressId);
}
