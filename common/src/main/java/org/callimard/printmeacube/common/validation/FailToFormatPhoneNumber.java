package org.callimard.printmeacube.common.validation;

public class FailToFormatPhoneNumber extends PhoneNumberException {

    public FailToFormatPhoneNumber(String message) {
        super(message);
    }

    public FailToFormatPhoneNumber(Throwable t) {
        super(t);
    }
}
