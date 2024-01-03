package com.book.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BookStoreApiException extends RuntimeException {
    private HttpStatus status;
    private String message;

    public BookStoreApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.message = message;
    }
}
