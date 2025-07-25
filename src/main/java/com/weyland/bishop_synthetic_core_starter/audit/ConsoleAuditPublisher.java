package com.weyland.bishop_synthetic_core_starter.audit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@ConditionalOnMissingBean(AuditPublisher.class)
//@ConditionalOnProperty(name="audit.kafka.enabled", havingValue = "false")
public class ConsoleAuditPublisher implements AuditPublisher {
    @Override
    public void publish(String message) {
        log.info("[AUDIT-CONSOLE]: " + message);
    }
}
