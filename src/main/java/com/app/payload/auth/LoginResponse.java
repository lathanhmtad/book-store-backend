package com.app.payload.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseCookie;

@Data
@Builder
public class LoginResponse {
    private String accessToken;
    private final String tokenType = "Bearer";
    @JsonIgnore
    private ResponseCookie refreshTokenCookie;
}
