package com.weyland.bishop_synthetic_core_starter.service;

import com.weyland.bishop_synthetic_core_starter.annotation.WeylandWatchingYou;
import com.weyland.bishop_synthetic_core_starter.model.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class CommandQueueService {
    private final Map<String, AtomicInteger> completedByAuthor = new ConcurrentHashMap<>();
    private final int MAX_QUEUE_SIZE = 100;

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2,
            4,
            60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(MAX_QUEUE_SIZE),
            new ThreadPoolExecutor.AbortPolicy()
    );

    @WeylandWatchingYou
    public void sumbit(Command command) {
        try{
            executor.execute(() -> executeCommand(command));
        }catch (RejectedExecutionException e) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);
        }
    }

    private void executeCommand(Command command) {
        completedByAuthor.computeIfAbsent(command.getAuthor(), k -> new AtomicInteger()).incrementAndGet();
        log.info("[COMMON] Executing command {}", command);
    }


    public int getQueueSize() {
        return executor.getQueue().size();
    }

    public Map<String, Integer> getCompletedPerAuthor() {
        Map<String, Integer> result = new HashMap<>();
        completedByAuthor.forEach((k, v) -> result.put(k, v.get()));
        return result;
    }
}
