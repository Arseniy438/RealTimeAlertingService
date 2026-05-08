package com.example.project.domain.rules.conditions;

import lombok.Getter;

@Getter
public class LessThanCondition implements Condition{

    private final double threshold;

    public LessThanCondition(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean evaluate(double actualValue) {
        return actualValue<threshold;
    }

    @Override
    public ConditionType getType() {
        return ConditionType.LESS_THAN;
    }

    @Override
    public double getThreshold() {
        return threshold;
    }
}
