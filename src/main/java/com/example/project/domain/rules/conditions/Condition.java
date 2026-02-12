package com.example.project.domain.rules.conditions;

public interface Condition {

    boolean evaluate(double value);

    ConditionType type();

    double threshold();

    default Condition and(Condition other){
        return new Condition() {
            @Override
            public boolean evaluate(double value) {
                return Condition.this.evaluate(value) && other.evaluate(value);
            }

            @Override
            public ConditionType type() {
                return Condition.this.type();
            }

            @Override
            public double threshold() {
                return Condition.this.threshold();
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
            public ConditionType type() {
                return Condition.this.type();
            }

            @Override
            public double threshold() {
                return Condition.this.threshold();
            }
        };
    }

}
