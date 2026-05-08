package com.example.project.domain.rules.conditions;

public interface Condition {

    boolean evaluate(double value);

    ConditionType getType();

    double getThreshold();

    default Condition and(Condition other){
        return new Condition() {
            @Override
            public boolean evaluate(double value) {
                return Condition.this.evaluate(value) && other.evaluate(value);
            }

            @Override
            public ConditionType getType() {
                return Condition.this.getType();
            }

            @Override
            public double getThreshold() {
                return Condition.this.getThreshold();
            }
        };
    }

    default Condition or(Condition other){
        return new Condition() {
            @Override
            public boolean evaluate(double value) {
                return Condition.this.evaluate(value) || other.evaluate(value);
            }

            @Override
            public ConditionType getType() {
                return Condition.this.getType();
            }

            @Override
            public double getThreshold() {
                return Condition.this.getThreshold();
            }
        };
    }

}
