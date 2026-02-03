package com.example.project.domain.rules.conditions;

public class EqualsCondition implements Condition{
    private final double threshold;


    public EqualsCondition(double thresholdValue) {
        this.threshold = thresholdValue;
    }
    @Override
    public boolean evaluate(double actualValue){
        return threshold == actualValue;
    }
}
