package com.example.project.exceptions;

public class NotSupportedTypeException extends RuntimeException {
    public NotSupportedTypeException(String message) {
        super(message);
    }
}
