package org.example;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.example.audit.Audit;
import org.example.audit.AuditAspect;
import org.example.audit.AuthenticationFacade;
import org.example.entity.AuditLog;
import org.example.repository.AuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditAspectTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private AuditAspect auditAspect;

    @BeforeEach
    void setUp() {
        // Предполагаемые значения для authenticationFacade.getUsername()
        when(authenticationFacade.getUsername()).thenReturn("testUser");
    }

    @Test
    void audit_ShouldSaveAuditLog_WhenAnnotationPresent() throws Throwable {

        Audit auditAnnotation = mock(Audit.class);

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.getSignature()).thenReturn(mock(Signature.class));
        when(joinPoint.getSignature().toShortString()).thenReturn("testMethod");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"arg1", "arg2"});


        auditAspect.audit(joinPoint, auditAnnotation);

        verify(auditLogRepository, times(1)).save(any(AuditLog.class));
    }

    @Test
    void audit_ShouldSaveCorrectAuditLog_WhenAnnotationPresent() throws Throwable {

        Audit auditAnnotation = mock(Audit.class);

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.getSignature()).thenReturn(mock(Signature.class));
        when(joinPoint.getSignature().toShortString()).thenReturn("testMethod");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"arg1", "arg2"});


        auditAspect.audit(joinPoint, auditAnnotation);


        verify(auditLogRepository, times(1)).save(argThat(auditLog ->
                auditLog.getUsername().equals("testUser") &&
                        auditLog.getAction().equals("testMethod") &&
                        auditLog.getRequestParams().equals(Arrays.toString(new Object[]{"arg1", "arg2"})) &&
                        auditLog.getTimestamp() != null
        ));
    }
}
