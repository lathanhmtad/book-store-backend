package com.book.exception;

import com.book.payload.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ExceptionResponse> handleException(Exception exception, WebRequest webRequest,
                                                              String errorCode, HttpStatus status) {
        ExceptionResponse errorDetails = ExceptionResponse.builder()
                .time(LocalDateTime.now())
                .message(exception.getMessage())
                .path(webRequest.getDescription(true))
                .errorCode(errorCode)
                .build();
        return new ResponseEntity<>(errorDetails, status);
    }
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEmailNotFoundException(EmailNotFoundException exception, WebRequest webRequest) {
        return handleException(exception, webRequest, "EMAIL_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidCredentialsException(InvalidCredentialsException exception, WebRequest webRequest) {
        return handleException(exception, webRequest, "INVALID_PASSWORD", HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DisabledAccountException.class)
    public ResponseEntity<ExceptionResponse> handleDisabledAccountException(DisabledAccountException exception, WebRequest webRequest) {
        return handleException(exception, webRequest, "ACCOUNT_DISABLED", HttpStatus.FORBIDDEN);
    }
}
