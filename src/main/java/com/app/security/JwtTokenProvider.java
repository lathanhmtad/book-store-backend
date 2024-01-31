package com.app.security;

import com.app.util.TimeConverterUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${com.app.jwt.jwtSecret}")
    private String jwtSecret;

    @Value("${com.app.jwt.jwtExpiration}")
    private String jwtExpiration;

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            throw new JwtException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new JwtException("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT claims string is empty");
        }
    }
    public String generateTokenFromUsername(String username) {
        Date currentDate = new Date();
        long jwtExpirationMs = TimeConverterUtil.getMilliseconds(jwtExpiration);
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationMs);

        String token = Jwts.builder()
                .setIssuer("book-store")
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
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