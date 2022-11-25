package org.callimard.printmeacube.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.callimard.printmeacube.ApiV1;
import org.callimard.printmeacube.entities.sql.User;
import org.callimard.printmeacube.jwt.JwtAccount;
import org.callimard.printmeacube.jwt.JwtFactory;
import org.callimard.printmeacube.jwt.JwtTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiV1.AUTHENTICATION_URL)
public class AuthenticationController {

    // Variables.

    private final JwtFactory jwtFactory;

    // Methods.

    @PostMapping("/token")
    public JwtTokenResponse login(@AuthenticationPrincipal User user) {
        try {
            return new JwtTokenResponse(jwtFactory.generateJwtFor(Instant.now(), user));
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Fail during jwt creation", e);
        }
    }

    @PostMapping("/token/refresh")
    public JwtTokenResponse refreshToken(@AuthenticationPrincipal JwtAccount jwtAccount) {
        try {
            return new JwtTokenResponse(jwtFactory.generateJwtFor(Instant.now(), jwtAccount));
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Fail during jwt creation", e);
        }
    }
}
