package com.example.aicode.security.jwt;


import com.example.aicode.entity.User;
import com.example.aicode.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    private String buildToken(User user, long expiration, String type) {

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .claim("type", type)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateAccessToken(User user) {
        return buildToken(
                user,
                jwtProperties.getAccessTokenExpiration(),
                "ACCESS"
        );
    }


    public String generateRefreshToken(User user) {
        return buildToken(
                user,
                jwtProperties.getRefreshTokenExpiration(),
                "REFRESH"
        );
    }



    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token,
                                CustomUserDetails userDetails) {

        return extractUsername(token).equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver) {

        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                jwtProperties.getSecret().getBytes()
        );
    }

    public Instant getAccessTokenExpiry() {
        return Instant.ofEpochMilli(
                System.currentTimeMillis() +
                        jwtProperties.getAccessTokenExpiration()
        );
    }

    public Instant getRefreshTokenExpiry() {
        return Instant.ofEpochMilli(
                System.currentTimeMillis() +
                        jwtProperties.getRefreshTokenExpiration()
        );
    }

}