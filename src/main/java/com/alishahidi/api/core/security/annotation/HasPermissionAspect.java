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
public class HasPermissionAspect {
    @Before("@within(hasPermission) || @annotation(hasPermission)")
    public void hasPermissionMethod(JoinPoint joinPoint, HasPermission hasPermission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new SecurityException("Unauthorized: No authentication found.");
        }

        Set<String> permissionsRequired = new HashSet<>(Arrays.asList(hasPermission.value())).stream()
                .map(permission -> "PERMISSION_" + permission.name())
                .collect(Collectors.toSet());
        System.out.println(permissionsRequired);

        Set<String> userPermissions = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (!userPermissions.containsAll(permissionsRequired)) {
            throw new SecurityException("Unauthorized: Insufficient permission(s). Required: " + permissionsRequired);
        }
    }

    @Before("execution(* *(..)) && @within(hasPermission)")
    public void hasPermissionClass(JoinPoint joinPoint, HasPermission hasPermission) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        hasPermissionMethod(joinPoint, hasPermission);
    }
}
