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
    private ResponseEntity<ExceptionResponse> handleException(Exception exception, WebRequest webRequest, HttpStatus status) {
        ExceptionResponse errorDetails = ExceptionResponse.builder()
                .time(LocalDateTime.now())
                .message(exception.getMessage())
                .path(webRequest.getDescription(true))
                .errorCode(status.toString())
                .build();
        return new ResponseEntity<>(errorDetails, status);
    }
    @ExceptionHandler(BookStoreApiException.class)
    public ResponseEntity<ExceptionResponse> handleBookStoreApiException(BookStoreApiException exception, WebRequest webRequest) {
        return handleException(exception, webRequest, exception.getStatus());
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
        return handleException(exception, webRequest, HttpStatus.NOT_FOUND);
    }
}
