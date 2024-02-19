package com.app.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BookStoreApiException extends RuntimeException {
    private HttpStatus status;
    private String message;

    public BookStoreApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.message = message;
    }
}
