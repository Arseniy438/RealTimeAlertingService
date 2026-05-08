package com.example.project.dto.request;

import com.example.project.domain.rules.conditions.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConditionRequestMapper {
    default Condition toDomain(ConditionRequest dto) {
        if (dto == null) {
            return null;
        }

        switch (dto.type()) {
            case GREATER_THAN:
                return new GreaterThanCondition(dto.threshold());
            case LESS_THAN:
                return new LessThanCondition(dto.threshold());
            case EQUALS:
                return new EqualsCondition(dto.threshold());
            default:
                throw new IllegalArgumentException("Unknown condition type: " + dto.type());
        }
    }
}