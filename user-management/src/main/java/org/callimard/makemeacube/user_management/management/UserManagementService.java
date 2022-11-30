package org.callimard.makemeacube.user_management.management;

import com.google.common.collect.Lists;
import org.callimard.makemeacube.common.validation.ValidEmail;
import org.callimard.makemeacube.common.validation.ValidPassword;
import org.callimard.makemeacube.common.validation.ValidPhone;
import org.callimard.makemeacube.models.aop.Printer3DId;
import org.callimard.makemeacube.models.aop.UserAddressId;
import org.callimard.makemeacube.models.aop.UserId;
import org.callimard.makemeacube.models.sql.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public interface UserManagementService {

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

    record UserAddressInformationDTO(@NotNull @NotBlank String address,
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
                                    @NotNull @Valid UserAddressInformationDTO address,
                                    @NotNull @ValidPhone String phone,
                                    @NotNull @NotBlank String makerDescription) {
    }

    User makerUserRegistration(@NotNull @Valid MakerUserRegistrationDTO makerUserRegistrationDTO, @NotNull RegistrationProvider provider);

    User getUser(@NotNull @UserId Integer userId);

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

    User addUserAddress(@NotNull @UserId Integer userId, @NotNull @Valid UserAddressInformationDTO userAddressInformationDTO);

    User deleteUserAddress(@NotNull @UserId Integer userId, @NotNull @UserAddressId Integer userAddressId);

    record MaterialInformationDTO(@NotNull MaterialType type, @Size(max = 1000) String colors, @Size(max = 1000) String description) {

        public Material generateMaterial(MakerTool tool) {
            return new Material(-1, type, colors, description, tool);
        }
    }

    record Print3DInformationDTO(@NotNull @NotBlank @Size(min = 5, max = 255) String name,
                                 @NotNull @Size(max = 2000) String description,
                                 @Size(max = 3000) String reference,
                                 @NotNull @Size(min = 1) List<@Valid MaterialInformationDTO> materials,
                                 @NotNull @Min(0) Integer x,
                                 @NotNull @Min(0) Integer y,
                                 @NotNull @Min(0) Integer z,
                                 @NotNull @Min(0) Integer xAccuracy,
                                 @NotNull @Min(0) Integer yAccuracy,
                                 @NotNull @Min(0) Integer zAccuracy,
                                 @NotNull @Min(0) Integer layerThickness,
                                 @NotNull Printer3DType type) {

        public Printer3D generatePrinter3D(User user) {
            return new Printer3D(-1,
                                 user,
                                 name,
                                 description,
                                 reference,
                                 Lists.newArrayList(),
                                 x,
                                 y,
                                 z,
                                 xAccuracy,
                                 yAccuracy,
                                 zAccuracy,
                                 layerThickness,
                                 type);
        }
    }

    User addPrinter3D(@NotNull @UserId Integer userId, @NotNull @Valid Print3DInformationDTO print3DInformationDTO);

    User deleteMakerTool(@NotNull @UserId Integer userId, @NotNull @Printer3DId Integer printer3DId);
}
