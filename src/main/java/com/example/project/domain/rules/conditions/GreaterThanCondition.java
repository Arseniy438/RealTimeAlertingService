package com.example.project.domain.rules.conditions;

import lombok.Getter;

@Getter
public class GreaterThanCondition implements Condition{

    private final double threshold;

    public GreaterThanCondition(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean evaluate(double actualValue) {
        return actualValue>threshold;
    }

    @Override
    public ConditionType getType() {
        return ConditionType.GREATER_THAN;
    }

    @Override
    public double getThreshold() {
        return threshold;
    }
}
