package org.callimard.makemeacube.common.validation;

import lombok.extern.slf4j.Slf4j;

import javax.swing.text.MaskFormatter;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PhoneVerifier implements ConstraintValidator<ValidPhone, String> {

    @Override
    public boolean isValid(String phoneNumberToVerify, ConstraintValidatorContext context) {
        return phoneNumberToVerify != null && !phoneNumberToVerify.isBlank() && PhoneVerifier.isCorrectAndSupportedPhoneNumber(phoneNumberToVerify);
    }

    // Singleton.

    private static final PhoneVerifier PHONE_TOOL = new PhoneVerifier();

    // Singleton variables.

    private final Map<String, PhoneFormatter> phoneFormatterMap;

    // FRANCE.
    public static final String FRANCE = "FRANCE";
    public static final String FRENCH_PHONE_FORMAT_MASK = "+## # ## ## ## ##";
    public static final String FRENCH_PHONE_FORMAT_REGEX = "(\\+33) [0-9]( [0-9]{2}){4}";
    public static final String FRENCH_PHONE_CODE = "33";

    // US.
    public static final String US = "US";
    public static final String US_PHONE_FORMAT_MASK = "+# (###) ###-####";
    public static final String US_PHONE_FORMAT_REGEX = "(\\+1) (\\([0-9]{3}\\)) [0-9]{3}-[0-9]{4}";
    public static final String US_PHONE_CODE = "1";

    // Constructors.

    private PhoneVerifier() {
        phoneFormatterMap = new HashMap<>();
        fillPhoneFormatter();
    }

    // Singleton methods.

    private void fillPhoneFormatter() {
        phoneFormatterMap.put(FRENCH_PHONE_CODE, new FrenchPhoneFormatter());
        phoneFormatterMap.put(US_PHONE_CODE, new USPhoneFormatter());
    }

    // Tool methods.

    public static boolean isCorrectAndSupportedPhoneNumber(String supposedPhoneNumber) {
        String phone = extractDigit(supposedPhoneNumber);
        if (isAcceptedString(supposedPhoneNumber) && !phone.isBlank()) {
            PhoneFormatter phoneFormatter = searchPhoneFormatterFor(phone);
            if (phoneFormatter != null) {
                return phoneFormatter.canFormat(phone);
            } else
                return false;
        } else
            return false;
    }

    private static PhoneFormatter searchPhoneFormatterFor(String phone) {
        for (PhoneFormatter phoneFormatter : PHONE_TOOL.phoneFormatterMap.values()) {
            if (phoneFormatter.support(phone))
                return phoneFormatter;
        }
        return null;
    }

    public static String permissiveFormatPhone(String phone) throws NotPhoneNumberException, UnsupportedPhoneNumberException,
                                                                    FailToFormatPhoneNumber {
        if (!isAcceptedString(phone))
            throw new NotPhoneNumberException();

        phone = extractDigit(phone);
        if (phone.isBlank())
            throw new NotPhoneNumberException();

        for (PhoneFormatter pFormatter : PHONE_TOOL.phoneFormatterMap.values()) {
            if (pFormatter.support(phone))
                return pFormatter.permissiveFormat(phone);
        }

        log.debug("No PhoneFormatter find for the phone number -> {}", phone);
        throw new UnsupportedPhoneNumberException("The phone number is not supported");
    }

    /**
     * Try to find a {@link PhoneFormatter} which support the phone number. The first {@code PhoneVerifier.PhoneFormatter} find which support the
     * phone number will try to format it. If the formation success, returns the formatted phone number, else return null.
     * <p>
     * If there is no {@code PhoneVerifier.PhoneFormatter} which support the phone number return null.
     *
     * @param phone the phone number to format
     *
     * @return the formatted phone number if the formation success, else null.
     */
    public static String formatPhone(String phone) throws UnsupportedPhoneNumberException, NotPhoneNumberException, FailToFormatPhoneNumber {
        if (!isAcceptedString(phone))
            throw new NotPhoneNumberException();

        phone = extractDigit(phone);
        if (phone.isBlank())
            throw new NotPhoneNumberException();

        for (PhoneFormatter pFormatter : PHONE_TOOL.phoneFormatterMap.values()) {
            if (pFormatter.support(phone))
                return pFormatter.format(phone);
        }

        throw new UnsupportedPhoneNumberException("The phone number is not supported");
    }

    private static boolean isAcceptedString(String phone) {
        return phone != null && !phone.isBlank();
    }

    private static String extractDigit(String phone) {
        return phone.replaceAll("[^0-9]+", "");
    }

    // Inner classes.

    private abstract static class PhoneFormatter {

        private String country;

        private MaskFormatter formatter;

        private String phoneRegex;

        private String countryPhoneCode;

        protected PhoneFormatter(String country, String formatMask, String phoneRegex, String countryPhoneCode) {
            try {
                this.country = country;

                formatter = new MaskFormatter(formatMask);
                formatter.setValueContainsLiteralCharacters(false);

                this.phoneRegex = phoneRegex;
                this.countryPhoneCode = countryPhoneCode;
            } catch (ParseException e) {
                log.error("MaskFormatter instantiation error for the class {}, MUST NEVER APPEND. StackTrace = {}", getClass(), e);
            }
        }

        /**
         * @param phone a phone number
         *
         * @return true if the {@code PhoneFormatter} can format the phone number.
         */
        public final boolean support(String phone) {
            return isAcceptedString(phone) && beginWithCorrectCode(phone);
        }

        private String permissiveFormat(String phone) throws FailToFormatPhoneNumber {
            try {
                return valueToString(phone);
            } catch (ParseException e) {
                logIfParseException(phone);
                throw new FailToFormatPhoneNumber(e);
            }
        }

        public boolean canFormat(String phone) {
            try {
                String formattedPhone = valueToString(phone);
                return formattedPhone.matches(phoneRegex);
            } catch (ParseException e) {
                return false;
            }
        }

        /**
         * @param phone the phone number to format
         *
         * @return the formatted phone number if the formation success, else null.
         */
        public String format(String phone) throws FailToFormatPhoneNumber {
            try {
                String formattedPhone = valueToString(phone);
                if (formattedPhone.matches(phoneRegex))
                    return formattedPhone;
                else
                    throw new FailToFormatPhoneNumber(("Result formatted phone number does not math with the correct regex (result phone number = " +
                            "%s, expected regex = %s").formatted(formattedPhone, phoneRegex));
            } catch (ParseException e) {
                logIfParseException(phone);
                throw new FailToFormatPhoneNumber(e);
            }
        }

        private boolean beginWithCorrectCode(String phone) {
            return phone.startsWith(countryPhoneCode);
        }

        private void logIfParseException(String phone) {
            log.error("Error while trying to format a {} phone. The phone to format {} ({} phone format -> {})", country, phone, country,
                      FRENCH_PHONE_FORMAT_MASK);
        }

        private String valueToString(String value) throws ParseException {
            return formatter.valueToString(value);
        }
    }

    // Phone Formatters.

    private static class FrenchPhoneFormatter extends PhoneFormatter {
        public FrenchPhoneFormatter() {
            super(FRANCE, FRENCH_PHONE_FORMAT_MASK, FRENCH_PHONE_FORMAT_REGEX, FRENCH_PHONE_CODE);
        }
    }

    private static class USPhoneFormatter extends PhoneFormatter {
        public USPhoneFormatter() {
            super(US, US_PHONE_FORMAT_MASK, US_PHONE_FORMAT_REGEX, US_PHONE_CODE);
        }
    }
}
