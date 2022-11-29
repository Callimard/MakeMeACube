package org.callimard.makemeacube.user_management.registration;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.callimard.makemeacube.models.aop.EntitySearchingAspect;
import org.callimard.makemeacube.models.aop.SearchUsers;
import org.callimard.makemeacube.models.aop.UserAddressId;
import org.callimard.makemeacube.models.aop.UserId;
import org.callimard.makemeacube.models.sql.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    private final UserAddressRepository userAddressRepository;

    private final EntitySearchingAspect entitySearchingAspect;

    // Methods.

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User basicUserRegistration(@NotNull @Valid BasicUserRegistrationDTO userInfo, @NotNull RegistrationProvider provider) {
        // TODO Manage the email verification
        var user = new User(userInfo.mail(), userInfo.pseudo(),
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
        var user = generateUser(makerInfo);
        return userRepository.save(user);
    }

    private User generateUser(MakerUserRegistrationDTO makerInfo) {
        return new User(-1,
                        makerInfo.mail(),
                        makerInfo.pseudo(),
                        passwordEncoder.encode(makerInfo.password()),
                        makerInfo.firstName(),
                        makerInfo.lastName(),
                        Lists.newArrayList(),
                        makerInfo.phone(),
                        true,
                        makerInfo.makerDescription(),
                        Lists.newArrayList(),
                        RegistrationProvider.LOCAL,
                        Instant.now());
    }

    private UserAddress createAndSaveUserAddress(User user, AddressInformationDTO addressInformationDTO) {
        var userAddress = addressInformationDTO.generateUserAddress(user);
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

    @SearchUsers
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User updateUserInformation(@NotNull @UserId Integer userId, @NotNull @Valid UserUpdatedInformation userUpdatedInformation) {
        var user = entitySearchingAspect.entityOf(User.class);
        var updatedUser = userUpdatedInformation.updatedUser(user);
        return userRepository.save(updatedUser);
    }

    @SearchUsers
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User addUserAddress(@NotNull @UserId Integer userId, @NotNull @Valid AddressInformationDTO addressInformationDTO) {
        var user = entitySearchingAspect.entityOf(User.class);

        var address = addressInformationDTO.generateUserAddress(user);
        address = userAddressRepository.save(address);

        user.getAddresses().add(address);
        return userRepository.save(user);
    }

    @SearchUsers
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User deleteUserAddress(@NotNull @UserId Integer userId, @NotNull @UserAddressId Integer userAddressId) {
        var user = entitySearchingAspect.entityOf(User.class);
        var userAddress = user.getUserAddressWith(userAddressId);

        if (userAddress.isPresent()) {
            user.removeUserAddressWith(userAddressId);
            user = userRepository.save(user);
            userAddressRepository.delete(userAddress.get());
        }

        return user;
    }
}
