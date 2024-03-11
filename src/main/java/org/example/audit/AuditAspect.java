package org.example.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.entity.AuditLog;
import org.example.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        // Получение объекта аутентификации из контекста безопасности
        Authentication authentication = authenticationFacade.getAuthentication();

        // Проверка, имеет ли пользователь доступ к выполнению действия
//        boolean hasAccess = checkAccess(authentication, audit.requiredRole());
//        System.out.println(authentication.getAuthorities());

        AuditLog auditLog = new AuditLog(LocalDateTime.now(), username, action, requestParams);
        auditLogRepository.save(auditLog);

        return joinPoint.proceed();
    }

//    private boolean checkAccess(Authentication authentication, String requiredRole) {
//        // Проверка, имеет ли пользователь требуемую роль доступа
//        return authentication.getAuthorities().stream()
//                .anyMatch(authority -> authority.getAuthority().equals(requiredRole));
//    }
}
