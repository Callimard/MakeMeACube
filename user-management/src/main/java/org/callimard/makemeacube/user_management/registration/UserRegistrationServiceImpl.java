package org.callimard.makemeacube.user_management.registration;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.callimard.makemeacube.common.RegistrationProvider;
import org.callimard.makemeacube.entities.aop.EntitySearchWithIdAspect;
import org.callimard.makemeacube.entities.aop.SearchUsers;
import org.callimard.makemeacube.entities.aop.UserId;
import org.callimard.makemeacube.entities.sql.User;
import org.callimard.makemeacube.entities.sql.UserAddress;
import org.callimard.makemeacube.entities.sql.UserAddressRepository;
import org.callimard.makemeacube.entities.sql.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Validated
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    // Variables.

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    private final EntitySearchWithIdAspect entitySearchWithIdAspect;

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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User updateUserInformation(@NotNull @UserId Integer userId, @NotNull @Valid UserUpdatedInformation userUpdatedInformation) {
        List<User> users = entitySearchWithIdAspect.entitiesOf(User.class);
        var updatedUser = userUpdatedInformation.updatedUser(users.get(0));
        return userRepository.save(updatedUser);
    }

    @SearchUsers
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User addUserAddress(@NotNull @UserId Integer userId, @NotNull @Valid AddressInformationDTO addressInformationDTO) {
        List<User> users = entitySearchWithIdAspect.entitiesOf(User.class);
        var user = users.get(0);

        var address = addressInformationDTO.generateUserAddress(user);
        address = userAddressRepository.save(address);

        user.getAddresses().add(address);
        return userRepository.save(user);
    }
}
