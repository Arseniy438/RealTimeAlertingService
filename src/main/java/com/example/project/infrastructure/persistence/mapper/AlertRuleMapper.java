package com.example.project.infrastructure.persistence.mapper;

import com.example.project.domain.rules.AlertRule;
import com.example.project.infrastructure.persistence.AlertRuleEntity;

public class AlertRuleMapper {


    public AlertRuleEntity toEntity(AlertRule domain) {
        return new AlertRuleEntity(domain.getName(), domain.isEnabled(), domain.getSourceType(),domain.getSourceName(),
                domain.getSeverity(), domain.getCondition(), domain.getCreatedAt());
    }

    public AlertRule toDomain(AlertRuleEntity entity) {
        return new AlertRule(entity.getId(), entity.getName(), entity.isEnabled(), entity.getType(), entity.getField(), entity.getSeverity(),
                entity.getCondition(), entity.getCreatedAt());
    }


}
