package com.app.payload.auth;
import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
