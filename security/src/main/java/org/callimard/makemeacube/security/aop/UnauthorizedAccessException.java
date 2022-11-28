package org.callimard.makemeacube.security.aop;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedAccessException extends RuntimeException {

    // Constructors.

    public UnauthorizedAccessException() {
        super();
    }
}
