package com.example.project.infrastructure.persistence.mapper;

import com.example.project.domain.rules.AlertRule;
import com.example.project.domain.rules.conditions.*;
import com.example.project.infrastructure.persistence.AlertRuleEntity;
import org.springframework.stereotype.Component;

@Component
public class AlertRuleMapper {

    public AlertRuleEntity toEntity(AlertRule domain) {
        return new AlertRuleEntity(
                domain.getName(),
                domain.isEnabled(),
                domain.getSourceType(),
                domain.getSourceName(),
                domain.getSeverity(),
                domain.getCondition().getType(),
                domain.getCondition().getThreshold(),
                domain.getDescription(),
                domain.getCooldownInSeconds(),
                domain.getMaxRetries(),
                domain.getComparisonWindow(),
                domain.getCreatedAt()
        );
    }

    public AlertRule toDomain(AlertRuleEntity entity) {
        AlertRule rule = new AlertRule(
                entity.getId(),
                entity.getName(),
                entity.isEnabled(),
                entity.getType(),
                entity.getField(),
                entity.getSeverity(),
                toCondition(entity.getConditionType(), entity.getConditionThreshold()),
                entity.getCreatedAt()
        );
        rule.setDescription(entity.getDescription());
        rule.setCooldownInSeconds(entity.getCooldownInSeconds());
        rule.setMaxRetries(entity.getMaxRetries());
        rule.setComparisonWindow(entity.getComparisonWindowSeconds());
        return rule;
    }

    private Condition toCondition(ConditionType type, double threshold) {
        return switch (type) {
            case GREATER_THAN -> new GreaterThanCondition(threshold);
            case LESS_THAN -> new LessThanCondition(threshold);
            case EQUALS -> new EqualsCondition(threshold);
        };
    }
}
