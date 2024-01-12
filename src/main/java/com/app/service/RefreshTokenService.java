package com.app.service;

import com.app.entity.RefreshToken;

public interface RefreshTokenService {
    RefreshToken saveRefreshToken(String username);
    RefreshToken verifyExpiration(RefreshToken token);
}
