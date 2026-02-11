package com.example.project.infrastructure.persistence.mapper;

import com.example.project.domain.alerts.Alert;
import com.example.project.infrastructure.persistence.AlertEntity;

public class AlertMapper {

    public AlertEntity toEntity(Alert domain) {
        return new AlertEntity(domain.getRule(),domain.getCreatedAt(), domain.getUpdatedAt(),
                domain.getStatus(),domain.getSeverity(),domain.getEvent(),domain.getRetryCount(), domain.getLastTriggeredAt());
    }

    public Alert toDomain(AlertEntity entity) {
        return new Alert(entity.getId(), entity.getRuleId(), entity.getEvent(), entity.getSeverity(), entity.getRetryCount(), entity.getLastTriggeredAt());
    }

}
