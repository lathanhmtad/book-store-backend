package com.app.security;

import com.app.constant.AppConstant;
import com.app.entity.RefreshToken;
import com.app.entity.User;
import com.app.exception.TokenRefreshException;
import com.app.repository.RefreshTokenRepo;
import com.app.util.TimeConverterUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import java.util.Date;
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
        Date currentDate = new Date();
        if(token.getExpiryDate().compareTo(currentDate) < 0) {
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

    @Transactional
    public void deleteByUserId(Long userId) {
        try {
            refreshTokenRepository.deleteByUser_Id(userId);
        }
         catch (Exception e) {
            logger.severe(e.getMessage());
         }
    }

    private RefreshToken saveRefreshToken(Long userId) {
        long refreshTokenDurationMs = TimeConverterUtil.getMilliseconds(refreshTokenDuration);

        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUser_Id(userId);

        if(optionalRefreshToken.isPresent()) {
            RefreshToken refreshTokenAlreadyExist = optionalRefreshToken.get();

            // update the expiration date field
            refreshTokenAlreadyExist.setExpiryDate(calculateExpiryDate(new Date(), refreshTokenDurationMs));
            return refreshTokenRepository.save(refreshTokenAlreadyExist);
        }
        else {
            // if refresh token does not exist yet => create new
            RefreshToken newRefreshToken = RefreshToken.builder()
                    .user(new User(userId))
                    .expiryDate(calculateExpiryDate(new Date(), refreshTokenDurationMs))
                    .token(UUID.randomUUID().toString())
                    .build();

            RefreshToken savedRefreshToken = refreshTokenRepository.save(newRefreshToken);
            return savedRefreshToken;
        }
    }

    private Date calculateExpiryDate(Date currentDate, long durationMs) {
        return new Date(currentDate.getTime() + durationMs);
    }
}
