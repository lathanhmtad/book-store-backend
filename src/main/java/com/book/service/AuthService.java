package com.book.service;

import com.book.payload.LoginResponse;

public interface AuthService {
    LoginResponse authenticate(String email, String password);
}
