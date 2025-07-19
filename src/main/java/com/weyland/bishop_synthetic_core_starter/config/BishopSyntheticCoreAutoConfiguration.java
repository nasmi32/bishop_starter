package com.weyland.bishop_synthetic_core_starter.config;

import com.weyland.bishop_synthetic_core_starter.audit.AuditAspect;
import com.weyland.bishop_synthetic_core_starter.audit.AuditPublisher;
import com.weyland.bishop_synthetic_core_starter.service.CommandQueueService;
import com.weyland.bishop_synthetic_core_starter.service.CommandService;
import com.weyland.bishop_synthetic_core_starter.service.MetricsService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
public class BishopSyntheticCoreAutoConfiguration {

    @Bean
    public AuditAspect auditAspect(AuditPublisher auditPublisher) {
        return new AuditAspect(auditPublisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public CommandQueueService commandQueue() {
        return new CommandQueueService();
    }

    @Bean
    public CommandService commandService(CommandQueueService commandQueueService, MetricsService metricsService) {
        return new CommandService(commandQueueService, metricsService);
    }

    @Bean
    public MetricsService metricsService(CommandQueueService commandQueueService, MeterRegistry meterRegistry) {
        return new MetricsService(commandQueueService, meterRegistry);
    }

}