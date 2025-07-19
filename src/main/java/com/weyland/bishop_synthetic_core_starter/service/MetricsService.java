package com.weyland.bishop_synthetic_core_starter.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MetricsService {
    private final CommandQueueService commandQueueService;
    private MeterRegistry meterRegistry;
    private final Map<String, Counter> authorCounters = new ConcurrentHashMap<>();

    public MetricsService(CommandQueueService commandQueueService, MeterRegistry meterRegistry) {
        this.commandQueueService = commandQueueService;
        this.meterRegistry = meterRegistry;

        Gauge.builder("command.queue.size", commandQueueService, CommandQueueService::getQueueSize)
                .description("Current queue size")
                .register(meterRegistry);

//        Gauge.builder("command.author.requests", () -> 0)
//                .description("Number of requests by author")
//                .register(meterRegistry);
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("queueSize", commandQueueService.getQueueSize());
        metrics.put("completedByAuthor", commandQueueService.getCompletedPerAuthor());
        return metrics;
    }

    public void incrementAuthorCommandCounter(String author) {
        authorCounters.computeIfAbsent(author, a ->
                Counter.builder("commands.processed")
                        .tag("author", a)
                        .description("Number of commands processed")
                        .register(meterRegistry))
                .increment();
    }

    @Scheduled(fixedRate = 1000)
    public void updateQueueSize() {
        int queueSize = commandQueueService.getQueueSize();
        Gauge.builder("command.queue.size", queueSize, Number::intValue)
                .description("Current queue size")
                .register(meterRegistry);
        log.info("Updating queue size {}", queueSize);
    }
}
