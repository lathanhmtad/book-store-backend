package com.app.security;

import com.app.constant.AppConstant;
import com.app.entity.RefreshToken;
import com.app.entity.User;
import com.app.exception.ResourceNotFoundException;
import com.app.exception.TokenRefreshException;
import com.app.repository.RefreshTokenRepo;
import com.app.utils.TimeConverterUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class RefreshTokenProvider {
    @Value("${com.app.jwt.jwtRefreshExpiration}")
    private String refreshTokenDuration;
    private RefreshTokenRepo refreshTokenRepository;

    private Logger logger = Logger.getLogger(RefreshTokenProvider.class.getName());
    public RefreshTokenProvider(RefreshTokenRepo refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public ResponseCookie generateRefreshTokenCookie(Long userId) {
        RefreshToken refreshToken = this.saveRefreshToken(userId);
        return ResponseCookie.from(AppConstant.NAME_REFRESH_TOKEN_COOKIE, refreshToken.getToken())
                .path("/")
                .httpOnly(true)
                .build();
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new login request");
        }
        return token;
    }

    public String getRefreshTokenFromCookies(HttpServletRequest servletRequest) {
        Cookie cookie = WebUtils.getCookie(servletRequest, AppConstant.NAME_REFRESH_TOKEN_COOKIE);
        return cookie != null ? cookie.getValue() : null;
    }
    public ResponseCookie getCleanRefreshTokenCookie() {
        ResponseCookie cookie = ResponseCookie.from(AppConstant.NAME_REFRESH_TOKEN_COOKIE, null).path("/").build();
        return cookie;
    }

    private RefreshToken saveRefreshToken(Long userId) {
        long refreshTokenDurationMs = TimeConverterUtil.getMilliseconds(refreshTokenDuration);
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUser_Id(userId);

        if(optionalRefreshToken.isPresent()) {
            RefreshToken refreshTokenAlreadyExist = optionalRefreshToken.get();

            // update the expiration date field
            refreshTokenAlreadyExist.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            return refreshTokenRepository.save(refreshTokenAlreadyExist);
        }
        else {
            RefreshToken newRefreshToken = new RefreshToken();
            newRefreshToken.setUser(
                    User
                            .builder()
                            .id(userId)
                            .build());
            newRefreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            newRefreshToken.setToken(UUID.randomUUID().toString());

            RefreshToken savedRefreshToken = refreshTokenRepository.save(newRefreshToken);
            return savedRefreshToken;
        }

    }

    @Transactional
    public void deleteByUserId(Long userId) {
        try {
            refreshTokenRepository.deleteByUser_Id(userId);
        }
         catch (Exception e) {
            logger.severe(e.getMessage());
         }
    }
}
