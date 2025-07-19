package com.weyland.bishop_synthetic_core_starter.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "audit.kafka.enabled", havingValue = "true")
public class KafkaDockerComposeConfig {

    @Value("${audit.kafka.compose-path}")
    private String composeFilePath;

    @PostConstruct
    public void startKafka() {
        try {
            log.info("Kafka start with docker-compose: {}", composeFilePath);
            ProcessBuilder pb = new ProcessBuilder("docker-compose", "-f", composeFilePath, "up", "-d");
            pb.directory(new File("."));
            pb.inheritIO();
            Process process = pb.start();
            boolean completed = process.waitFor() == 0;
            if (completed) {
                log.info("Kafka started successfully with docker-compose");
            } else {
                log.warn("Process docker-compose finished with error");
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error starting Kafka with docker-compose", e);
        }
    }
}