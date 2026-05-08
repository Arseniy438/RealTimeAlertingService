package com.example.project.dto.response;

import com.example.project.domain.alerts.Alert;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {AlertRuleResponseMapper.class, EventResponseMapper.class})
public interface AlertResponseMapper {
    AlertResponse toAlertResponse(Alert alert);
}