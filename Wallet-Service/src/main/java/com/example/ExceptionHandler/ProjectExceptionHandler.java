package com.example.ExceptionHandler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ProjectExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse("An error occurred", ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse("Access denied", ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse("Authentication failed", ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }
}
