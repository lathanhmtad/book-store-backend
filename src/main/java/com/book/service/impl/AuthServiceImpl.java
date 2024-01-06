package com.book.service.impl;

import com.book.exception.BookStoreApiException;
import com.book.payload.auth.LoginResponse;
import com.book.service.AuthService;
import com.book.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private PasswordEncoder passwordEncoder;
    @Override
    public LoginResponse authenticate(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BookStoreApiException("Invalid password", HttpStatus.BAD_REQUEST);
        }
        if(!userDetails.isEnabled()) {
            throw new BookStoreApiException("Account disabled", HttpStatus.FORBIDDEN);
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String token = jwtTokenProvider.generateJwtToken(authentication);
        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(token)
                .build();
        return loginResponse;
    }
}
