package org.callimard.printmeacube.user_management.registration;

import lombok.RequiredArgsConstructor;
import org.callimard.printmeacube.ApiV1;
import org.callimard.printmeacube.common.RegistrationProvider;
import org.callimard.printmeacube.entities.dto.UserDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
