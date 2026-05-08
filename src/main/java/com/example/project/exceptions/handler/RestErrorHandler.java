package com.example.project.exceptions.handler;

import com.example.project.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class RestErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EventMappingException.class)
    public ResponseEntity<String> handleEventMappingException(EventMappingException ex) {
        //TODO("Implement specific handler logic")
        log.error("Event mapping exception: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RuleNotFoundException.class)
    public ResponseEntity<String> handleRuleNotFoundException(RuleNotFoundException ex) {
        //TODO("Implement specific handler logic")
        log.info("Rule not found exception: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlertNotFoundException.class)
    public ResponseEntity<String> handleAlertNotFoundException(AlertNotFoundException ex) {
        //TODO("Implement specific handler logic")
        log.info("Alert not found exception: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<String> handleEventNotFoundException(EventNotFoundException ex) {
        //TODO("Implement specific handler logic")
        log.info("Event not found exception: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotSupportedTypeException.class)
    public ResponseEntity<String> handleNotSupportedTypeException(NotSupportedTypeException ex) {
        //TODO("Implement specific handler logic")
        log.info("Type not supported exception: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsuitableStatusException.class)
    public ResponseEntity<String> handleUnsuitableStatusException(UnsuitableStatusException ex) {
        //TODO("Implement specific handler logic")
        log.info("Unsuitable status exception: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }



}