package com.lucaspaiola.restapispringboot.exception;

import com.lucaspaiola.restapispringboot.error.FieldErrorResponse;
import com.lucaspaiola.restapispringboot.error.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        List<FieldErrorResponse> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorResponse(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        ValidationErrorResponse response = new ValidationErrorResponse(
                "Validation failed",
                errors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ValidationErrorResponse> handleMessageNotReadableException(
            HttpMessageNotReadableException exception
    ) {

        if (exception.getCause() instanceof InvalidFormatException invalidFormatException) {

            String field = invalidFormatException.getPath().getFirst().getPropertyName();

            if (invalidFormatException.getTargetType().isEnum()) {

                Object[] enumValues = invalidFormatException
                        .getTargetType()
                        .getEnumConstants();

                String acceptedValues = Arrays.stream(enumValues)
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));

                FieldErrorResponse error = new FieldErrorResponse(
                        field,
                        "Invalid value. Accepted values: " + acceptedValues
                );

                ValidationErrorResponse response = new ValidationErrorResponse(
                        "Invalid request",
                        List.of(error)
                );

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }
        }

        ValidationErrorResponse response = new ValidationErrorResponse(
                "Invalid request body",
                List.of()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ValidationErrorResponse> handleAlreadyExists(AlreadyExistsException exception) {
        ValidationErrorResponse response = new ValidationErrorResponse(
                exception.getMessage(),
                List.of()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }
}
