package com.refactoringhabit.common.exception;

import java.util.StringJoiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> methodArgumentValidExceptionHandler(
            MethodArgumentNotValidException e) {

        log.error("[methodArgumentValidExceptionHandler] ex", e);
        String message = getMessage(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of("E001", message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> httpMessageNotReadableExceptionHandler(
            HttpMessageNotReadableException e) {

        log.error("[httpMessageNotReadableExceptionHandler] ex", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of("E002", e.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> httpRequestMethodNotSupportedExceptionHandler(
            HttpRequestMethodNotSupportedException e) {

        log.error("[HttpRequestMethodNotSupportedException] ex", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of("E003", e.getMessage()));
    }

    @ExceptionHandler(MissingPathVariableException.class)
    protected ResponseEntity<ErrorResponse> missingPathVariableExceptionHandler(
            MissingPathVariableException e) {

        log.error("[HttpRequestMethodNotSupportedException] ex", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of("E004", e.getMessage()));

    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {

        return ResponseEntity.status(e.getErrorType().getHttpStatus())
                .body(ErrorResponse.of(e.getErrorType()));
    }

    private String getMessage(MethodArgumentNotValidException e) {

        StringJoiner joiner = new StringJoiner(", ");

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            String message =
                    "[" + fieldError.getField() + "]은(는) " + fieldError.getDefaultMessage();
            joiner.add(message);
        }
        return joiner.toString();
    }
}
