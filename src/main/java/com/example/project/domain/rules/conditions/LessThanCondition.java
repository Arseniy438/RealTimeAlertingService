package com.example.project.domain.rules.conditions;

public class LessThanCondition implements Condition{

    private final double threshold;

    public LessThanCondition(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean evaluate(double actualValue) {
        return actualValue<threshold;
    }
}
