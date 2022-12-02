package org.callimard.makemeacube.user_management.management;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.callimard.makemeacube.models.aop.*;
import org.callimard.makemeacube.models.sql.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Validated
@Service
public class UserManagementServiceImpl implements UserManagementService {

    // Variables.

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final MakerToolRepository makerToolRepository;
    private final Printer3DRepository printer3DRepository;
    private final MaterialRepository materialRepository;
    private final UserEvaluationRepository userEvaluationRepository;

    private final EntitySearchingAspect entitySearchingAspect;

    // Methods.

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User basicUserRegistration(@NotNull @Valid BasicUserRegistrationDTO userInfo, @NotNull RegistrationProvider provider) {
        // TODO Manage the email verification
        var user = new User(userInfo.mail().toLowerCase(Locale.FRENCH), userInfo.pseudo().toLowerCase(Locale.FRENCH),
                            passwordEncoder.encode(userInfo.password()), provider, Instant.now());
        return userRepository.save(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User makerUserRegistration(@NotNull @Valid MakerUserRegistrationDTO makerInfo, @NotNull RegistrationProvider provider) {
        // TODO Manage the email verification
        var user = createAndSaveUser(makerInfo);
        UserAddress userAddress = createAndSaveUserAddress(user, makerInfo.address());
        return addUserAddressToUser(user, userAddress);
    }

    private User createAndSaveUser(MakerUserRegistrationDTO makerInfo) {
        var maker = generateMaker(makerInfo);
        return userRepository.save(maker);
    }

    private User generateMaker(MakerUserRegistrationDTO makerInfo) {
        return new User(-1,
                        makerInfo.mail().toLowerCase(Locale.FRENCH),
                        makerInfo.pseudo().toLowerCase(Locale.FRENCH),
                        passwordEncoder.encode(makerInfo.password()),
                        makerInfo.firstName(),
                        makerInfo.lastName(),
                        Lists.newArrayList(),
                        makerInfo.phone(),
                        true,
                        makerInfo.makerDescription(),
                        Lists.newArrayList(),
                        Lists.newArrayList(),
                        Lists.newArrayList(),
                        RegistrationProvider.LOCAL,
                        Instant.now());
    }

    private UserAddress createAndSaveUserAddress(User user, UserAddressInformationDTO userAddressInformationDTO) {
        var userAddress = userAddressInformationDTO.generateUserAddress(user);
        userAddress = userAddressRepository.save(userAddress);
        return userAddress;
    }

    private User addUserAddressToUser(User user, UserAddress userAddress) {
        user.getAddresses().add(userAddress);
        return userRepository.save(user);
    }

    @SearchUsers
    @Override
    public User getUser(@NotNull @UserId Integer userId) {
        return entitySearchingAspect.entityOf(User.class);
    }

    @Override
    public List<User> searchMaker(String mail, String pseudo, List<MaterialType> materialTypes,
                                  List<String> materialColors, List<Printer3DType> printer3DTypes) {

        var builder = new UserSpecificationBuilder();

        if (mail != null)
            builder.addEqualMailSpecification(mail);

        if (pseudo != null)
            builder.addEqualPseudoSpecification(pseudo);

        if (materialTypes != null && !materialTypes.isEmpty())
            builder.addEqualMaterialTypesSpecification(materialTypes);

        if (materialColors != null && !materialColors.isEmpty())
            builder.addMaterialWithColorSpecifications(materialColors);

        if (printer3DTypes != null && !printer3DTypes.isEmpty())
            builder.addPrinter3DTypeSpecification(printer3DTypes);

        var specification = builder.build();

        if (specification.isEmpty()) {
            return Lists.newArrayList();
        }

        return userRepository.findAll(specification.get());
    }

    @SearchUsers
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User updateUserInformation(@NotNull @UserId Integer userId, @NotNull @Valid UserUpdatedInformation userUpdatedInformation) {
        var user = entitySearchingAspect.entityOf(User.class);
        userUpdatedInformation.updatedUser(user);
        return userRepository.save(user);
    }

    @SearchUsers
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User addUserAddress(@NotNull @UserId Integer userId,
                               @NotNull @Valid UserManagementService.UserAddressInformationDTO userAddressInformationDTO) {
        var user = entitySearchingAspect.entityOf(User.class);
        user.getAddresses().add(userAddressRepository.save(userAddressInformationDTO.generateUserAddress(user)));
        return userRepository.save(user);
    }

    @SearchUsers
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User updateUserAddress(@NotNull @UserId Integer userId, @NotNull @UserAddressId Integer userAddressId,
                                  @NotNull @Valid UserAddressInformationDTO userAddressInformationDTO) {
        var user = entitySearchingAspect.entityOf(User.class);
        var address = user.getUserAddressWith(userAddressId);

        if (address.isPresent()) {
            userAddressInformationDTO.updateUserAddress(address.get()); // User entity is also updated by reference
            userAddressRepository.save(address.get());
        }

        return user;
    }

    @SearchUsers
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User deleteUserAddress(@NotNull @UserId Integer userId, @NotNull @UserAddressId Integer userAddressId) {
        var user = entitySearchingAspect.entityOf(User.class);
        var userAddress = user.getUserAddressWith(userAddressId);

        if (userAddress.isPresent()) {
            user.removeUserAddressWith(userAddressId);
            userAddressRepository.delete(userAddress.get());
        }

        return user;
    }

    @SearchUsers
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User addPrinter3D(@NotNull @UserId Integer userId, @NotNull @Valid UserManagementService.Print3DInformationDTO print3DInformationDTO) {
        var user = entitySearchingAspect.entityOf(User.class);
        var printer3D = printer3DRepository.save(print3DInformationDTO.generatePrinter3D(user));
        printer3D = addMaterialsToPrinter3D(printer3D, saveAllMaterials(print3DInformationDTO.materials(), printer3D));
        user.getTools().add(printer3D);
        return userRepository.save(user);
    }

    private Printer3D addMaterialsToPrinter3D(Printer3D printer3D, List<Material> materials) {
        printer3D.getMaterials().addAll(materials);
        return printer3DRepository.save(printer3D);
    }

    private List<Material> saveAllMaterials(List<MaterialInformationDTO> materialsInformation, Printer3D printer3D) {
        List<Material> materials = Lists.newArrayList();
        for (MaterialInformationDTO materialInformationDTO : materialsInformation) {
            materials.add(materialRepository.save(materialInformationDTO.generateMaterial(printer3D)));
        }
        return materials;
    }

    @SearchUsers
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User updatePrinter3D(@NotNull @UserId Integer userId, @NotNull @Printer3DId Integer printer3DId,
                                @NotNull @Valid Print3DInformationDTO print3DInformationDTO) {
        var user = entitySearchingAspect.entityOf(User.class);
        var printer3D = user.getMakerTool(printer3DId, Printer3D.class);

        if (printer3D.isPresent()) {
            materialRepository.deleteAll(printer3D.get().getMaterials());
            print3DInformationDTO.updatePrinter3D(printer3D.get()); // User entity is also updated by reference
            var savedMaterials = materialRepository.saveAll(printer3D.get().getMaterials());
            printer3D.get().setMaterials(savedMaterials);
            printer3DRepository.save(printer3D.get());
        }

        return user;
    }

    @SearchUsers
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User deleteMakerTool(@NotNull @UserId Integer userId, @NotNull @MakerToolId Integer makerToolId) {
        var user = entitySearchingAspect.entityOf(User.class);
        var makerTool = user.getMakerToolWith(makerToolId);

        if (makerTool.isPresent()) {
            user.removeMakerToolWith(makerToolId);
            materialRepository.deleteAll(makerTool.get().getMaterials());
            makerToolRepository.delete(makerTool.get());
        }

        return user;
    }

    @SearchUsers
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User addUserEvaluation(@NotNull @UserId Integer userId, @NotNull @UserId Integer evaluatedUserId,
                                  @NotNull @Valid UserManagementService.UserEvaluationInformationDTO userEvaluationInformationDTO) {
        var users = entitySearchingAspect.entitiesOf(User.class);

        var evaluator = users.get(0);
        var evaluated = users.get(1);

        userEvaluationRepository.save(userEvaluationInformationDTO.generateUserGrade(evaluator, evaluated));

        return evaluator;
    }

    @SearchUsers
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User updateUserEvaluation(@NotNull @UserId Integer userId, @NotNull @UserId Integer evaluatedUserId,
                                     @NotNull @UserEvaluationId Integer userEvaluationId,
                                     @NotNull @Valid UserEvaluationInformationDTO userEvaluationInformationDTO) {
        var user = entitySearchingAspect.entityOf(User.class);
        var userEvaluation = user.getUserEvaluations(userEvaluationId);

        if (userEvaluation.isPresent() && userEvaluation.get().getEvaluated().getId().equals(evaluatedUserId)) {
            userEvaluationInformationDTO.updateUserEvaluation(userEvaluation.get());
            userEvaluationRepository.save(userEvaluation.get());
        }

        return user;
    }

    @SearchUsers
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User deleteUserEvaluation(@NotNull @UserId Integer userId, @NotNull @UserId Integer evaluatedUserId,
                                     @NotNull @UserEvaluationId Integer userEvaluationId) {
        var user = entitySearchingAspect.entityOf(User.class);
        var userEvaluation = user.getUserEvaluations(userEvaluationId);

        if (userEvaluation.isPresent() && userEvaluation.get().getEvaluated().getId().equals(evaluatedUserId)) {
            userEvaluationRepository.delete(userEvaluation.get());
            user.removeUserEvaluation(userEvaluationId);
        }

        return user;
    }
}
