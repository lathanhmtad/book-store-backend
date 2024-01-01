package com.book.exception;

public class EmailNotFoundException extends ResourceNotFoundException {
    public EmailNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(resourceName, fieldName, fieldValue);
    }
}
