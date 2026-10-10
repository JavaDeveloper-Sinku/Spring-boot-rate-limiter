
package com.sinku.ratelimiter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixedWindowRateLimiterTest {

    private FixedWindowRateLimiter rateLimiter;

    @BeforeEach
    void setUp() {
        rateLimiter = new FixedWindowRateLimiter();
    }

    @Test
    void shouldAllowFirstFiveRequests() {
        for (int i = 0; i < 5; i++) {
            var decision = rateLimiter.check("user-1");

            assertTrue(decision.allowed());
        }
    }

    @Test
    void shouldRejectSixthRequest() {
        for (int i = 0; i < 5; i++) {
            rateLimiter.check("user-1");
        }

        var decision = rateLimiter.check("user-1");

        assertFalse(decision.allowed());
        assertTrue(decision.retryAfterSeconds() > 0);
    }

    @Test
    void shouldTrackDifferentUsersIndependently() {
        for (int i = 0; i < 5; i++) {
            rateLimiter.check("user-1");
        }

        assertFalse(rateLimiter.check("user-1").allowed());
        assertTrue(rateLimiter.check("user-2").allowed());
    }
}