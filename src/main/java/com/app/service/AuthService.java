package com.app.service;

import com.app.payload.auth.LoginResponse;
import com.app.payload.auth.TokenRefreshResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

public interface AuthService {
    LoginResponse authenticateAndGenerateToken(String email, String password);
    TokenRefreshResponse processRefreshToken(HttpServletRequest request);
    ResponseCookie doLogout(Long userId);
}
