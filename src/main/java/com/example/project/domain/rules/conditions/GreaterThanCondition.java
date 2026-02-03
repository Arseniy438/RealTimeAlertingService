package com.example.project.domain.rules.conditions;

public class GreaterThanCondition implements Condition{

    private final double threshold;

    public GreaterThanCondition(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean evaluate(double actualValue) {
        return actualValue>threshold;
    }
}
