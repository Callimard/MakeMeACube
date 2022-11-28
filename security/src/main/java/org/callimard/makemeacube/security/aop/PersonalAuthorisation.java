package org.callimard.makemeacube.security.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PersonalAuthorisation {

    /**
     * @return the parameter name which is the user id
     */
    String userIdParameterName() default "userId";
}
