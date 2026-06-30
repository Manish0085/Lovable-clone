package com.example.aicode.dto.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public record AuthenticationResponse(

        String accessToken,

        String refreshToken,

        Instant expiresAt,

        Instant refreshTokenExpiresAt,

        UserResponse user

) {
}