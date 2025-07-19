package com.weyland.bishop_synthetic_core_starter.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import java.util.Arrays;

@Slf4j
@Aspect
public class AuditAspect {
    private final AuditPublisher auditPublisher;

    public AuditAspect(AuditPublisher auditPublisher) {
        this.auditPublisher = auditPublisher;
    }

    @AfterReturning(pointcut = "@annotation(com.weyland.bishop_synthetic_core_starter.annotation.WeylandWatchingYou)", returning = "result")
    public void audit(JoinPoint joinPoint, Object result) throws JsonProcessingException {
        String message = String.format("AUDIT >> %s | args: %s | result: %s",
                joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs()),
                result);
        auditPublisher.publish(message);
    }
}
