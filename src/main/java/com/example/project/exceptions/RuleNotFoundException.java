package com.example.project.exceptions;

public class RuleNotFoundException extends RuntimeException{

    public RuleNotFoundException(String message){
        super(message);
    }
}
