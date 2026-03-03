package com.example.RealTimeAlertingService;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@TestConfiguration
public class FixedClockConfig {

    @Bean
    public Clock testClock(){
        return Clock.fixed(Instant.parse("2026-03-01T10:00:00Z"),
                ZoneOffset.UTC);
    }

}
