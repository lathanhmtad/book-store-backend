package com.app.controller;

import com.app.payload.auth.LoginRequest;
import com.app.payload.auth.LoginResponse;
import com.app.payload.auth.TokenRefreshResponse;
import com.app.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private AuthService authService;
    @GetMapping("/logout/{id}")
    public ResponseEntity<?> logout(@PathVariable("id") Long userId) {
        ResponseCookie clearCookie = authService.doLogout(userId);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.authenticateAndGenerateToken(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, loginResponse.getRefreshTokenCookie().toString())
                .body(loginResponse);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponse> refreshToken(HttpServletRequest request) {
        TokenRefreshResponse res = authService.processRefreshToken(request);
        return ResponseEntity.ok(res);
    }
}

