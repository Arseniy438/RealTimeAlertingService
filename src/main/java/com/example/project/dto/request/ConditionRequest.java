package com.example.project.dto.request;

import com.example.project.domain.rules.conditions.ConditionType;
import jakarta.validation.constraints.NotNull;

public record ConditionRequest(@NotNull ConditionType type, Double threshold){
}
