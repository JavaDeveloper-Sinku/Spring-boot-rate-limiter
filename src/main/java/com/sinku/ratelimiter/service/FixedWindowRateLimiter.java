
package com.sinku.ratelimiter.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class FixedWindowRateLimiter {

    private static final int LIMIT = 5;
    private static final long WINDOW_MS = 10_000;

    private final ConcurrentMap<String, WindowState> windows =
            new ConcurrentHashMap<>();

    public Decision check(String key) {
        long now = System.currentTimeMillis();

        WindowState state = windows.computeIfAbsent(
                key, ignored -> new WindowState(now)
        );

        synchronized (state) {
            if (now - state.windowStart >= WINDOW_MS) {
                state.windowStart = now;
                state.count = 0;
            }

            if (state.count < LIMIT) {
                state.count++;

                return new Decision(true, 0);
            }

            long remainingMs =
                    WINDOW_MS - (now - state.windowStart);

            long retryAfter = Math.max(
                    1, (remainingMs + 999) / 1000
            );

            return new Decision(false, retryAfter);
        }
    }

    private static class WindowState {
        private long windowStart;
        private int count;

        private WindowState(long now) {
            this.windowStart = now;
        }
    }

    public record Decision(
            boolean allowed,
            long retryAfterSeconds
    ) {}
}