package com.weyland.bishop_synthetic_core_starter.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
//@ConditionalOnProperty(name="audit.kafka.enabled", havingValue = "true")
public class KafkaAuditPublisher implements AuditPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public KafkaAuditPublisher(KafkaTemplate<String, String> kafkaTemplate, String topic) {
        log.info("KafkaAuditPublisher created");
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(String message) {
        log.info("Sending message to Kafka: topic={}, message={}", topic, message);
        kafkaTemplate.send(topic, message);
    }
}
