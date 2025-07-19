package com.weyland.bishop_synthetic_core_starter.config;

import com.weyland.bishop_synthetic_core_starter.audit.AuditPublisher;
import com.weyland.bishop_synthetic_core_starter.audit.ConsoleAuditPublisher;
import com.weyland.bishop_synthetic_core_starter.audit.KafkaAuditPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class AuditPublisherAutoConfig {

    @Bean
    @ConditionalOnProperty(name = "audit.kafka.enabled", havingValue = "true")
    public AuditPublisher kafkaAuditPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            @Value("${audit.kafka.topic}") String topic
    ) {
        return new KafkaAuditPublisher(kafkaTemplate, topic);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuditPublisher consoleAuditPublisher() {
        return new ConsoleAuditPublisher();
    }
}