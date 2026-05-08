package com.example.project.dto.response;

import com.example.project.domain.rules.AlertRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING, uses = {ConditionResponseMapper.class})
public interface AlertRuleResponseMapper {
    @Mapping(target = "comparisonWindow", expression = "java(java.time.Duration.ofMillis(alertRule.getComparisonWindow()))")
    AlertRuleResponse toAlertRuleResponse(AlertRule alertRule);
}