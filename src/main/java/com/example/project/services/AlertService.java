package com.example.project.services;

import com.example.project.domain.alerts.Alert;
import com.example.project.domain.events.EventType;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

@Service
public class AlertService {

    private final Clock clock;

    public AlertService(Clock clock) {
        this.clock = clock;
    }

    public void processEvent(Alert alert) {
        Instant now = Instant.now(clock);

        if (!alert.canRetry() && !alert.isFailed()) {
            alert.failed(now);
        }

        if (alert.getEvent().getType() == EventType.RECOVERY) {
            alert.resolve(now);
        }
    }
}
