package com.weyland.bishop_synthetic_core_starter.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.MultiGauge;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class MetricsService {
    private final CommandQueueService commandQueueService;
    private final MultiGauge authorGauge;

    public MetricsService(CommandQueueService commandQueueService, MeterRegistry meterRegistry) {
        this.commandQueueService = commandQueueService;

        Gauge.builder("command.queue.size", commandQueueService, CommandQueueService::getQueueSize)
                .description("Current queue size")
                .register(meterRegistry);

        this.authorGauge = MultiGauge.builder("command.completed.by.author")
                .description("Amount of completed commands by author")
                .register(meterRegistry);

        updateAuthorMetrics();
    }

    @Scheduled(fixedRate = 1000)
    public void updateAuthorMetrics() {
        Map<String, Integer> completed = commandQueueService.getCompletedPerAuthor();

        List<MultiGauge.Row<?>> rows = completed.entrySet().stream()
                .map(entry ->
                        MultiGauge.Row.of(
                                Tags.of("author", entry.getKey()),
                                entry.getValue()
                        )
                )
                .collect(Collectors.toList());

        authorGauge.register(rows, true);
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("queueSize", commandQueueService.getQueueSize());
        metrics.put("completedByAuthor", commandQueueService.getCompletedPerAuthor());
        return metrics;
    }
}
