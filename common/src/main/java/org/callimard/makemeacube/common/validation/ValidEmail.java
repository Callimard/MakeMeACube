package org.callimard.makemeacube.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailVerifier.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
    String message() default "Email has a wrong format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
