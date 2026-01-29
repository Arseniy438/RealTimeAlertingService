package com.example.project.repository;

import com.example.project.model.alerts.Alert;
import com.example.project.model.alerts.AlertStatus;
import com.example.project.model.rules.AlertRule;
//import jakarta.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class AlertRepository implements IAlertRepository {

    private final HashMap<Long, Alert> alertMap = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public void saveAlert(Alert alert) {
        if (alert.getId() == null) {
            alert.setId(idGenerator.getAndIncrement());
        }
        alertMap.put(alert.getId(), alert);
    }

    @Override
    public void updateAlert(Alert alert){
        alertMap.put(alert.getId(), alert);
    }

    @Override
    public Optional<Alert> getAlert(Long id) {
        return Optional.ofNullable(alertMap.get(id));
    }

    @Override
    public void deleteAlert(Long id) {
        if (alertMap.containsKey(id)) {
            alertMap.remove(id);
        }
    }

    public List<Alert> getAllAlert(){
        return alertMap.values().stream().toList();
    }

    @Override
    public Optional<Alert> findActiveByRule(AlertRule rule) {
        return alertMap.values().stream()
                .filter(alert -> alert.getRule().getId().equals(rule.getId()))
                .filter(alert -> alert.getStatus() == AlertStatus.ACTIVE)
                .findFirst();
    }
}
