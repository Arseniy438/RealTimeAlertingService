package com.example.project.services;

import com.example.project.domain.alerts.Alert;
import com.example.project.domain.events.EventType;
import com.example.project.domain.repository.AlertRepository;
import com.example.project.domain.rules.Severity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final Clock clock;
    private final AlertRepository alertRepository;

    public void processEvent(Alert alert) {
        Instant now = Instant.now(clock);

        if (!alert.canRetry() && !alert.isFailed()) {
            alert.failed(now);
        }
        if(alert.getRule().getSeverity() == Severity.CRITICAL){
            alert.getRule().setMaxRetries(5);
        }

        if (alert.getEvent().getType() == EventType.RECOVERY) {
            alert.resolve(now);
        }
    }

    @Transactional
    public Alert getAlert(Long id){
        return alertRepository.getAlert(id).orElseThrow(() -> new NoSuchElementException("Not found alert with id: \" + id"));
    }

    public void deleteAlert(Long id){
        alertRepository.deleteAlert(id);
    }

}
