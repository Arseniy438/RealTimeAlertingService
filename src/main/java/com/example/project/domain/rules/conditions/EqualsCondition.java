package com.example.project.domain.rules.conditions;

import lombok.Getter;

@Getter
public class EqualsCondition implements Condition{
    private final double threshold;


    public EqualsCondition(double thresholdValue) {
        this.threshold = thresholdValue;
    }
    @Override
    public boolean evaluate(double actualValue){
        return threshold == actualValue;
    }

    @Override
    public ConditionType getType() {
        return ConditionType.EQUALS;
    }

    @Override
    public double getThreshold() {
        return threshold;
    }
}
