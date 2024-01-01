package com.book.service.impl;

import com.book.payload.LoginResponse;
import com.book.exception.DisabledAccountException;
import com.book.exception.InvalidCredentialsException;
import com.book.service.AuthService;
import com.book.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;
    @Override
    public LoginResponse authenticate(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        if(!userDetails.isEnabled()) {
            throw new DisabledAccountException("Your account has not been activated");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtUtils.generateJwtToken();

        LoginResponse result = LoginResponse.builder()
                .accessToken(jwtToken)
                .email(userDetails.getUsername())
                .build();

        return result;
    }
}
