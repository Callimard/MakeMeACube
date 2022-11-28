package org.callimard.makemeacube.jwt.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.callimard.makemeacube.jwt.JwtAccount;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(0)
public class JwtAuthenticationAspect {

    // Methods.

    @Before("@annotation(org.callimard.makemeacube.jwt.aop.RequiresJwtAuthentication)")
    public void requiresJwtAuthentication() {
        if (!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof JwtAccount)) {
            throw new JwtAuthenticationRequiredException();
        }
    }
}
