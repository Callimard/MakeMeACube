package org.callimard.makemeacube.common.aop;

public class AopMethodSignatureException extends RuntimeException {
    public AopMethodSignatureException(String message) {
        super(message);
    }

    public AopMethodSignatureException(Throwable cause) {
        super(cause);
    }
}
