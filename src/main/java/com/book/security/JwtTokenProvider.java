package com.book.security;

import com.book.exception.BookStoreApiException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${com.book.jwtSecret}")
    private String jwtSecret;

    @Value("${com.book.jwtExpirationMs}")
    private int jwtExpirationMs;

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            throw new BookStoreApiException("Invalid JWT token", HttpStatus.BAD_REQUEST);
        } catch (ExpiredJwtException e) {
            throw new BookStoreApiException("Expired JWT token", HttpStatus.BAD_REQUEST);
        } catch (UnsupportedJwtException e) {
            throw new BookStoreApiException("Unsupported JWT token", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            throw new BookStoreApiException("JWT claims string is empty", HttpStatus.BAD_REQUEST);
        }
    }
    public String generateJwtToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationMs);

        String token = Jwts.builder()
                .setIssuer("book-store")
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("authorities", authentication.getAuthorities().toString())
                .signWith(key())
                .compact();
        return token;
    }
    public String getUsername(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        return username;
    }
    private Key key() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

}