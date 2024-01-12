package com.app.service.impl;

import com.app.constant.AppConstant;
import com.app.entity.RefreshToken;
import com.app.exception.AccountBlockedException;
import com.app.exception.TokenRefreshException;
import com.app.payload.auth.LoginResponse;
import com.app.payload.auth.TokenRefreshResponse;
import com.app.repository.RefreshTokenRepo;
import com.app.security.MyUserDetails;
import com.app.service.AuthService;
import com.app.security.JwtTokenProvider;
import com.app.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private PasswordEncoder passwordEncoder;
    private RefreshTokenService refreshTokenService;
    private RefreshTokenRepo refreshTokenRepo;

    @Override
    public LoginResponse authenticateAndGenerateToken(String email, String password) {
        MyUserDetails userDetails = (MyUserDetails) userDetailsService.loadUserByUsername(email);

        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Login information is incorrect!");
        }
        if(!userDetails.isEnabled()) {
            throw new AccountBlockedException("Account blocked!");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        String accessToken = jwtTokenProvider.generateTokenFromUsername(email);

        ResponseCookie refreshTokenCookie = generateRefreshTokenCookie(userDetails.getUsername());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshTokenCookie(refreshTokenCookie)
                .build();
    }

    @Override
    public TokenRefreshResponse processRefreshToken(HttpServletRequest servletRequest) {

        String requestRefreshToken = jwtTokenProvider.getRefreshTokenFromCookies(servletRequest);
        if(requestRefreshToken == null) {
            throw new TokenRefreshException("Refresh token not found. Please make a new login request!");
        }

        return refreshTokenRepo.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtTokenProvider.generateTokenFromUsername(user.getEmail());
                    return TokenRefreshResponse.builder()
                            .accessToken(token)
                            .refreshToken(requestRefreshToken)
                            .build();
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @Override
    public void logout() {
        jwtTokenProvider.getCleanRefreshTokenCookie();
    }

    private ResponseCookie generateRefreshTokenCookie(String username) {
        RefreshToken refreshToken = refreshTokenService.saveRefreshToken(username);
        return ResponseCookie.from(AppConstant.NAME_REFRESH_TOKEN_COOKIE, refreshToken.getToken())
                .path("/")
                .maxAge(Duration.between(Instant.now(), refreshToken.getExpiryDate()))
                .httpOnly(true)
                .build();
    }
}
