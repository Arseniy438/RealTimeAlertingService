package com.example.project.exceptions;

public class UnsuitableStatusException extends RuntimeException {
    public UnsuitableStatusException(String message) {
        super(message);
    }
}
