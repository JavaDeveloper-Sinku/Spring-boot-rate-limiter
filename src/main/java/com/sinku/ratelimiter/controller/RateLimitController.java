
package com.sinku.ratelimiter.controller;

import com.sinku.ratelimiter.service.FixedWindowRateLimiter;
import com.sinku.ratelimiter.service.SlidingWindowRateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ratelimit")
public class RateLimitController {

    private final FixedWindowRateLimiter fixedWindow;
    private final SlidingWindowRateLimiter slidingWindow;

    public RateLimitController(
            FixedWindowRateLimiter fixedWindow,
            SlidingWindowRateLimiter slidingWindow) {
        this.fixedWindow = fixedWindow;
        this.slidingWindow = slidingWindow;
    }

    @GetMapping("/fixed")
    public ResponseEntity<Map<String, Object>> fixed(
            HttpServletRequest request) {

        var decision = fixedWindow.check(request.getRemoteAddr());

        return buildResponse(
                decision.allowed(),
                decision.retryAfterSeconds(),
                "Fixed Window"
        );
    }

    @GetMapping("/sliding")
    public ResponseEntity<Map<String, Object>> sliding(
            HttpServletRequest request) {

        var decision = slidingWindow.check(request.getRemoteAddr());

        return buildResponse(
                decision.allowed(),
                decision.retryAfterSeconds(),
                "Sliding Window"
        );
    }

    private ResponseEntity<Map<String, Object>> buildResponse(
            boolean allowed,
            long retryAfter,
            String algorithm) {

        if (allowed) {
            return ResponseEntity.ok(Map.of(
                    "algorithm", algorithm,
                    "status", "ALLOWED",
                    "message", "Request processed successfully"
            ));
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header("Retry-After", String.valueOf(retryAfter))
                .body(Map.of(
                        "algorithm", algorithm,
                        "status", "REJECTED",
                        "message", "Rate limit exceeded",
                        "retryAfterSeconds", retryAfter
                ));
    }
}