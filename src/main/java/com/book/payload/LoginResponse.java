package com.book.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Builder
public class LoginResponse {
    private String accessToken;
}
