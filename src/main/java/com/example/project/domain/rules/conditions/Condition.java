package com.example.project.domain.rules.conditions;

public interface Condition {

    boolean evaluate(double value);

    default Condition and(Condition other){
        return value -> this.evaluate(value) && other.evaluate(value);
    }

    default Condition or(Condition other){
        return value -> this.evaluate(value) || other.evaluate(value);
    }

}
