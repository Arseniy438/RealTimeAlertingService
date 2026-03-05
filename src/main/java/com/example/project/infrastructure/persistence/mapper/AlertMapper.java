package com.example.project.infrastructure.persistence.mapper;

import com.example.project.domain.alerts.Alert;
import com.example.project.domain.events.Event;
import com.example.project.domain.rules.AlertRule;
import com.example.project.infrastructure.persistence.AlertEntity;
import com.example.project.infrastructure.persistence.AlertRuleEntity;
import com.example.project.infrastructure.persistence.EventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertMapper {

    private final AlertRuleMapper alertRuleMapper;
    private final EventMapper eventMapper;

    public AlertEntity toEntity(Alert domain, AlertRuleEntity alertRuleEntity, EventEntity eventEntity) {

        AlertEntity entity = new AlertEntity(
                alertRuleEntity,
                eventEntity,
                domain.getMessage() == null ? "" : domain.getMessage(),
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                domain.getStatus(),
                domain.getSeverity(),
                domain.getRetryCount(),
                domain.getLastTriggeredAt()
        );
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        return entity;
    }

    public Alert toDomain(AlertEntity entity) {
        return new Alert(
                entity.getId(),
                alertRuleMapper.toDomain(entity.getRule()),
                eventMapper.toDomain(entity.getEvent()),
                entity.getMessage(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getStatus(),
                entity.getSeverity(),
                entity.getRetryCount(),
                entity.getLastTriggeredAt()
        );
    }

}
