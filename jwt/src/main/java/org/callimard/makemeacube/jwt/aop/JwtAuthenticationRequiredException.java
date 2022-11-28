package org.callimard.makemeacube.jwt.aop;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JwtAuthenticationRequiredException extends RuntimeException {

    // Constructors.

    public JwtAuthenticationRequiredException() {
        super("Jwt authentication required");
    }
}
