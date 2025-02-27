package com.alishahidi.api.core.security.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Aspect
public class HasRoleAspect {
    @Before("@within(hasRole) || @annotation(hasRole)")
    public void hasRoleMethod(JoinPoint joinPoint, HasRole hasRole) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new SecurityException("Unauthorized: No authentication found.");
        }

        Set<String> rolesRequired = new HashSet<>(Arrays.asList(hasRole.value())).stream()
                .map(role -> "ROLE_" + role.name())
                .collect(Collectors.toSet());

        Set<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (!userRoles.containsAll(rolesRequired)) {
            throw new SecurityException("Unauthorized: Insufficient role(s). Required: " + rolesRequired);
        }
    }

    @Before("execution(* *(..)) && @within(hasRole)")
    public void hasRoleClass(JoinPoint joinPoint, HasRole hasRole) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        hasRoleMethod(joinPoint, hasRole);
    }
}
