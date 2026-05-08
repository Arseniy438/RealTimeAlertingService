package com.example.project.dto.response;


import com.example.project.domain.rules.conditions.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConditionResponseMapper {
    ConditionResponse toConditionResponse(Condition condition);
}
