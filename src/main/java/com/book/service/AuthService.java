package com.book.service;

import com.book.dto.request.LoginRequest;
import com.book.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse authenticate(LoginRequest loginRequest);
}
