
package com.sinku.ratelimiter.service;

import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class SlidingWindowRateLimiter {

    private static final int LIMIT = 5;
    private static final long WINDOW_MS = 10_000;

    private final ConcurrentMap<String, WindowState> windows =
            new ConcurrentHashMap<>();

    public Decision check(String key) {
        WindowState state = windows.computeIfAbsent(
                key, ignored -> new WindowState()
        );

        synchronized (state) {
            long now = System.currentTimeMillis();
            long cutoff = now - WINDOW_MS;

            // Remove requests older than the last 10 seconds.
            while (!state.timestamps.isEmpty()
                    && state.timestamps.peekFirst() <= cutoff) {
                state.timestamps.removeFirst();
            }

            if (state.timestamps.size() < LIMIT) {
                state.timestamps.addLast(now);

                return new Decision(true, 0);
            }

            long oldest = state.timestamps.peekFirst();
            long remainingMs = oldest + WINDOW_MS - now;

            long retryAfter = Math.max(
                    1, (remainingMs + 999) / 1000
            );

            return new Decision(false, retryAfter);
        }
    }

    private static class WindowState {
        private final Deque<Long> timestamps = new ArrayDeque<>();
    }

    public record Decision(
            boolean allowed,
            long retryAfterSeconds
    ) {}
}