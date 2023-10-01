package com.java.task.controllers;

import com.java.task.exceptions.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoParamInDataRequestException.class)
    public ResponseEntity<String> handleNoParamInDataRequestException(NoParamInDataRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: Invalid date format!");
    }

    @ExceptionHandler(TooYoungAgeException.class)
    public ResponseEntity<String> handleTooYoungAgeException(TooYoungAgeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Error: " + e.getMessage());
    }

    @ExceptionHandler(EmptyListException.class)
    public ResponseEntity<String> handleEmptyListException(EmptyListException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Error: " + e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParameter(MissingServletRequestParameterException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Required parameter '" + e.getParameterName() + "' is missing.");
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException e) {
        List<FieldError> errors = e.getErrors();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < errors.size(); i++) {
            FieldError error = errors.get(i);
            builder.append("Field: " + error.getField() + ". " + "Message: " + error.getDefaultMessage());
            if (i < errors.size() - 1) {
                builder.append(System.lineSeparator());
            }
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(builder.toString());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Entered email has already existed!");
    }


}
