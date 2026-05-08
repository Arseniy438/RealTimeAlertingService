package com.example.project.dto.response;

import com.example.project.domain.rules.conditions.ConditionType;

public record ConditionResponse(ConditionType type, double threshold) {

}
