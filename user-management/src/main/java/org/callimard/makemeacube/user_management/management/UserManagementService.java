package org.callimard.makemeacube.user_management.management;

import com.google.common.collect.Lists;
import org.callimard.makemeacube.common.validation.ValidEmail;
import org.callimard.makemeacube.common.validation.ValidPassword;
import org.callimard.makemeacube.common.validation.ValidPhone;
import org.callimard.makemeacube.models.aop.Printer3DId;
import org.callimard.makemeacube.models.aop.UserAddressId;
import org.callimard.makemeacube.models.aop.UserEvaluationId;
import org.callimard.makemeacube.models.aop.UserId;
import org.callimard.makemeacube.models.sql.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
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

        public void updateUserAddress(UserAddress userAddress) {
            userAddress.setAddress(address);
            userAddress.setCity(city);
            userAddress.setCountry(country);
            userAddress.setPostalCode(postalCode);
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

    List<User> searchMaker(String mail, String pseudo, List<MaterialType> materialTypes, List<String> materialColors,
                           List<Printer3DType> printer3DTypes);

    record UserUpdatedInformation(@NotNull @NotBlank @Size(min = 5, max = 255) String pseudo,
                                  @Size(max = 255) String firstName,
                                  @Size(max = 255) String lastName,
                                  @NotNull @ValidPhone String phone,
                                  String makerDescription,
                                  @NotNull Boolean isMaker) {

        public void updatedUser(User user) {
            user.setPseudo(pseudo);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);
            user.setMakerDescription(makerDescription);
            user.setIsMaker(isMaker);
        }

    }

    User updateUserInformation(@NotNull @UserId Integer userId, @NotNull @Valid UserUpdatedInformation userUpdatedInformation);

    User addUserAddress(@NotNull @UserId Integer userId, @NotNull @Valid UserAddressInformationDTO userAddressInformationDTO);

    User updateUserAddress(@NotNull @UserId Integer userId, @NotNull @UserAddressId Integer userAddressId,
                           @NotNull @Valid UserAddressInformationDTO userAddressInformationDTO);

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
                                 @NotNull @Min(1) Integer quantity,
                                 @NotNull @Min(0) Integer x,
                                 @NotNull @Min(0) Integer y,
                                 @NotNull @Min(0) Integer z,
                                 @NotNull @Min(0) Integer xAccuracy,
                                 @NotNull @Min(0) Integer yAccuracy,
                                 @NotNull @Min(0) Integer zAccuracy,
                                 @Size(max = 45) String resolution,
                                 @NotNull Printer3DType type) {

        public Printer3D generatePrinter3D(User user) {
            return new Printer3D(-1,
                                 user,
                                 name,
                                 description,
                                 reference,
                                 Lists.newArrayList(),
                                 quantity,
                                 x,
                                 y,
                                 z,
                                 xAccuracy,
                                 yAccuracy,
                                 zAccuracy,
                                 resolution,
                                 type);
        }

        /**
         * Update the {@link Printer3D}.
         * <p>
         * Concerning the update of materials, the materials list is replace by new materials however each new {@link Material} has as id the value
         * {@code -1}. Before save the updated Printer3D, new materials must be persisted.
         *
         * @param printer3D the Printer3D to update
         */
        public void updatePrinter3D(Printer3D printer3D) {
            printer3D.setName(name);
            printer3D.setDescription(description);
            printer3D.setReference(reference);
            printer3D.setMaterials(materials.stream().map(materialInformationDTO -> materialInformationDTO.generateMaterial(printer3D)).toList());
            printer3D.setQuantity(quantity);
            printer3D.setX(x);
            printer3D.setY(y);
            printer3D.setZ(z);
            printer3D.setXAccuracy(xAccuracy);
            printer3D.setYAccuracy(yAccuracy);
            printer3D.setZAccuracy(zAccuracy);
            printer3D.setResolution(resolution);
            printer3D.setType(type);
        }
    }

    User addPrinter3D(@NotNull @UserId Integer userId, @NotNull @Valid Print3DInformationDTO print3DInformationDTO);

    User updatePrinter3D(@NotNull @UserId Integer userId, @NotNull @Printer3DId Integer printer3DId,
                         @NotNull @Valid Print3DInformationDTO print3DInformationDTO);

    User deleteMakerTool(@NotNull @UserId Integer userId, @NotNull @Printer3DId Integer printer3DId);

    record UserEvaluationInformationDTO(@NotNull @Min(0) @Max(5) Integer grade, @Size(max = 300) String comment) {

        public UserEvaluation generateUserGrade(User evaluator, User evaluated) {
            return new UserEvaluation(-1, grade, comment, evaluator, evaluated);
        }

        public void updateUserEvaluation(UserEvaluation userEvaluation) {
            userEvaluation.setGrade(grade);
            userEvaluation.setComment(comment);
        }
    }

    User addUserEvaluation(@NotNull @UserId Integer userId, @NotNull @UserId Integer evaluatedUserId,
                           @NotNull @Valid UserEvaluationInformationDTO userEvaluationInformationDTO);

    User updateUserEvaluation(@NotNull @UserId Integer userId, @NotNull @UserId Integer evaluatedUserId,
                              @NotNull @UserEvaluationId Integer userEvaluationId,
                              @NotNull @Valid UserEvaluationInformationDTO userEvaluationInformationDTO);

    User deleteUserEvaluation(@NotNull @UserId Integer userId, @NotNull @UserId Integer evaluatedUserId,
                              @NotNull @UserEvaluationId Integer userEvaluationId);
}
