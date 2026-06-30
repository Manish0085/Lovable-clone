package com.example.aicode.service.impl;

import com.example.aicode.entity.RefreshToken;
import com.example.aicode.entity.User;
import com.example.aicode.exception.UnauthorizedException;
import com.example.aicode.repository.RefreshTokenRepository;
import com.example.aicode.security.jwt.JwtService;
import com.example.aicode.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtService jwtService;

    @Override
    @Transactional
    public RefreshToken create(User user, String token) {

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiresAt(jwtService.getRefreshTokenExpiry())
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public RefreshToken verify(String token) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new UnauthorizedException("Invalid refresh token."));

        if (refreshToken.isRevoked()) {
            throw new UnauthorizedException("Refresh token has been revoked.");
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new UnauthorizedException("Refresh token has expired.");
        }

        return refreshToken;
    }

    @Override
    @Transactional
    public RefreshToken rotate(RefreshToken oldToken) {

        User user = oldToken.getUser();

        String newToken =
                jwtService.generateRefreshToken(user);

        oldToken.setRevoked(true);

        oldToken.setReplacedByToken(newToken);

        refreshTokenRepository.save(oldToken);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(newToken)
                .expiresAt(jwtService.getRefreshTokenExpiry())
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void revokeAll(User user) {

    }
}
