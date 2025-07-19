package com.weyland.bishop_synthetic_core_starter.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import java.util.Arrays;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class AuditAspect {
    AuditPublisher auditPublisher;

    public AuditAspect(AuditPublisher auditPublisher) {
        this.auditPublisher = auditPublisher;
    }

    @AfterReturning(pointcut = "@annotation(com.weyland.bishop_synthetic_core_starter.annotation.WeylandWatchingYou)", returning = "result")
    public void audit(JoinPoint joinPoint, Object result) {
        String message = String.format("AUDIT >> %s | args: %s | result: %s",
                joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs()),
                result);
        auditPublisher.publish(message);

    }
}
