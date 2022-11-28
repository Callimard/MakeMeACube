package org.callimard.makemeacube.user_management.registration;

import lombok.RequiredArgsConstructor;
import org.callimard.makemeacube.common.RegistrationProvider;
import org.callimard.makemeacube.common.api.ApiV1;
import org.callimard.makemeacube.entities.dto.UserDTO;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiV1.USERS_URL)
public class UserRegistrationController {

    // Variables.

    private final UserRegistrationService userRegistrationService;

    // Methods.

    @PostMapping("/basic-registration")
    public UserDTO basicUserRegistration(@RequestBody UserRegistrationService.BasicUserRegistrationDTO basicUserRegistrationDTO) {
        return userRegistrationService.basicUserRegistration(basicUserRegistrationDTO, RegistrationProvider.LOCAL).toDTO();
    }

    @PostMapping("/maker-registration")
    public UserDTO makerUserRegistration(@RequestBody UserRegistrationService.MakerUserRegistrationDTO makerUserRegistrationDTO) {
        return userRegistrationService.makerUserRegistration(makerUserRegistrationDTO, RegistrationProvider.LOCAL).toDTO();
    }

    // TODO add real authorization
    @PutMapping("/{userId}")
    public UserDTO updateUserInformation(@PathVariable(name = "userId") Integer userId,
                                         @RequestBody UserRegistrationService.UserUpdatedInformation userUpdatedInformation) {
        return userRegistrationService.updateUserInformation(userId, userUpdatedInformation).toDTO();
    }

    // TODO add real authorization
    @PutMapping("/{userId}/addresses")
    public UserDTO updateUserAddresses(@PathVariable(name = "userId") Integer userId,
                                       @RequestBody UserRegistrationService.AddressInformationDTO addressInformationDTO) {
        return userRegistrationService.addUserAddress(userId, addressInformationDTO).toDTO();
    }
}
