package com.example.project.dto.request;

import com.example.project.domain.rules.AlertRule;
import com.example.project.domain.rules.conditions.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.time.Duration;
import java.time.Instant;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ConditionRequestMapper.class})
public interface AlertRuleRequestMapper {

//    AlertRule toDomain(AlertRuleRequest alertRuleRequest);

    default AlertRule toDomain(AlertRuleRequest request, ConditionRequestMapper conditionMapper) {
        Condition condition = conditionMapper.toDomain(request.getCondition());
        return new AlertRule(
                request.getName(),
                request.isEnabled(),
                request.getEventType(),
                request.getEventField(),
                request.getSeverity(),
                condition,
                Instant.now(),
                request.getDescription(),
                request.getCooldownInSeconds(),
                request.getMaxRetries(),
                Duration.ofMinutes(request.getComparisonWindow())
        );
        //String name, boolean enabled,
        // EventType eventType, EventField eventField
        // Severity severity, Condition condition,
        // Instant createdAt, String description,
        // int cooldownInSeconds, int maxRetries, Duration comparisonWindow
    }
}
