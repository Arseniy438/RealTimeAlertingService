package com.example.project.infrastructure.persistence.mapper;

import com.example.project.domain.alerts.Alert;
import com.example.project.infrastructure.persistence.AlertEntity;
import com.example.project.infrastructure.persistence.AlertRuleEntity;
import com.example.project.infrastructure.persistence.EventEntity;
import com.example.project.infrastructure.persistence.jpa.AlertRuleEntityRepository;
import com.example.project.infrastructure.persistence.jpa.EventEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class AlertMapper {

    private final AlertRuleMapper alertRuleMapper;
    private final EventMapper eventMapper;
    private final AlertRuleEntityRepository alertRuleEntityRepository;
    private final EventEntityRepository eventEntityRepository;

    public AlertMapper(
            AlertRuleMapper alertRuleMapper,
            EventMapper eventMapper,
            AlertRuleEntityRepository alertRuleEntityRepository,
            EventEntityRepository eventEntityRepository
    ) {
        this.alertRuleMapper = alertRuleMapper;
        this.eventMapper = eventMapper;
        this.alertRuleEntityRepository = alertRuleEntityRepository;
        this.eventEntityRepository = eventEntityRepository;
    }

    public AlertEntity toEntity(Alert domain) {
        AlertRuleEntity ruleEntity = resolveRuleEntity(domain.getRule());
        EventEntity eventEntity = resolveEventEntity(domain.getEvent());

        AlertEntity entity = new AlertEntity(
                ruleEntity,
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

    private AlertRuleEntity resolveRuleEntity(com.example.project.domain.rules.AlertRule rule) {
        if (rule.getId() != null) {
            return alertRuleEntityRepository.findById(rule.getId())
                    .orElseGet(() -> alertRuleMapper.toEntity(rule));
        }
        return alertRuleMapper.toEntity(rule);
    }

    private EventEntity resolveEventEntity(com.example.project.domain.events.Event event) {
        if (event.getId() != null) {
            return eventEntityRepository.findById(event.getId())
                    .orElseGet(() -> eventMapper.toEntity(event));
        }
        return eventMapper.toEntity(event);
    }
}
