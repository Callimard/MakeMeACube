package org.callimard.makemeacube.security.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.callimard.makemeacube.jwt.JwtAccount;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Aspect which propose Advice for manage authorisation. All join point suppose that the authentication is a Jwt authentication with a principal which
 * a {@link JwtAccount}.
 */
@Slf4j
@Component
@Aspect
@Order(15)
public class SecurityAspect {

    // Methods.

    @Before("@annotation(org.callimard.makemeacube.security.aop.PersonalAuthorisation)")
    public void personalAccessAuthorisation(JoinPoint joinPoint) {
        var jwtAccount = getJwtAccount();

        MethodSignature mSignature = (MethodSignature) joinPoint.getSignature();
        Method m = mSignature.getMethod();

        PersonalAuthorisation personalAuthorisation = m.getAnnotation(PersonalAuthorisation.class);
        int parameterIndex = getUserIdParameterIndex(mSignature, personalAuthorisation.userIdParameterName());

        if (!Objects.equals(jwtAccount.getId(), joinPoint.getArgs()[parameterIndex])) {
            throw new UnauthorizedAccessException();
        }
    }

    private static int getUserIdParameterIndex(MethodSignature mSignature, String userIdParamName) {
        int parameterIndex = -1;
        int counter = 0;
        for (String paramName : mSignature.getParameterNames()) {
            if (paramName.equals(userIdParamName)) {
                parameterIndex = counter;
                break;
            }
            counter++;
        }

        if (parameterIndex == -1) {
            throw new NoPersonalUserIdParameterFoundException();
        }
        return parameterIndex;
    }

    private JwtAccount getJwtAccount() {
        return (JwtAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
