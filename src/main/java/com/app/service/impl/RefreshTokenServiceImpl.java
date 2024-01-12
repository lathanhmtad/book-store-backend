package com.app.service.impl;

import com.app.entity.RefreshToken;
import com.app.entity.User;
import com.app.exception.TokenRefreshException;
import com.app.repository.RefreshTokenRepo;
import com.app.repository.UserRepo;
import com.app.service.RefreshTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${com.app.jwt.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;
    private RefreshTokenRepo refreshTokenRepository;
    private UserRepo userRepo;
    private ModelMapper modelMapper;
    public RefreshTokenServiceImpl(RefreshTokenRepo refreshTokenRepository, UserRepo userRepo, ModelMapper modelMapper) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public RefreshToken saveRefreshToken(String username) {
        User user = userRepo.findByEmail(username).get();
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository
                .findByUser(user)
                .map(refreshToken -> {
                    RefreshToken newRefreshToken = new RefreshToken();
                    if(refreshToken == null) {
                        newRefreshToken.setUser(user);
                        newRefreshToken.setToken(UUID.randomUUID().toString());
                    }
                    else {
                        modelMapper.map(refreshToken, newRefreshToken);
                    }
                    newRefreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));

                    RefreshToken savedRefreshToken = refreshTokenRepository.save(newRefreshToken);
                    return savedRefreshToken;
                });
        return optionalRefreshToken.get();
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new login request");
        }
        return token;
    }
}
