package com.example.project.repository;

import com.example.project.model.alerts.Alert;
import com.example.project.model.rules.AlertRule;

import java.util.List;
import java.util.Optional;

public interface IAlertRepository {

    void saveAlert(Alert alert);

    void updateAlert(Alert alert);

    Optional<Alert> getAlert(Long id);

    void deleteAlert(Long id);

    Optional<Alert> findActiveByRule(AlertRule rule);

    List<Alert> getAllAlert();
}
