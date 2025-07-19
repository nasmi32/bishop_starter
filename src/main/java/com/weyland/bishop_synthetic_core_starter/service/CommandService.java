package com.weyland.bishop_synthetic_core_starter.service;

import com.weyland.bishop_synthetic_core_starter.annotation.WeylandWatchingYou;
import com.weyland.bishop_synthetic_core_starter.model.Command;
import com.weyland.bishop_synthetic_core_starter.model.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

@Slf4j
public class CommandService {
    private final CommandQueueService commandQueueService;
    private final MetricsService metricsService;

    public CommandService(CommandQueueService commandQueueService, MetricsService metricsService) {
        this.commandQueueService = commandQueueService;
        this.metricsService = metricsService;
    }

    @WeylandWatchingYou
    public void processCommand(@Validated Command command) {
        if (command.getPriority() == Priority.CRITICAL) {
            log.info("[CRITICAL]: executing right now");
        }
        else {
            commandQueueService.sumbit(command);
        }
        metricsService.incrementAuthorCommandCounter(command.getAuthor());
        log.info("increment metric for author {}", command.getAuthor());

    }


}
