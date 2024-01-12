package com.app.exception;

public class AccountBlockedException extends BookStoreApiException {
    public AccountBlockedException(String message) {
        super(message);
    }
}
