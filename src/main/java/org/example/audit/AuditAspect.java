package org.example.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.entity.AuditLog;
import org.example.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@Aspect
public class AuditAspect {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Around("@annotation(audit)")
    public Object audit(ProceedingJoinPoint joinPoint, Audit audit) throws Throwable {
        String username = authenticationFacade.getUsername();
        String action = joinPoint.getSignature().toShortString();
        String requestParams = Arrays.toString(joinPoint.getArgs());

        AuditLog auditLog = new AuditLog(LocalDateTime.now(), username, action, requestParams);
        auditLogRepository.save(auditLog);
        return joinPoint.proceed();
    }

    private boolean hasRequiredRole(Authentication authentication, String[] requiredRoles) {
        for (String requiredRole : requiredRoles) {
            if (authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(requiredRole))) {
                return true;
            }
        }
        return false;
    }
}