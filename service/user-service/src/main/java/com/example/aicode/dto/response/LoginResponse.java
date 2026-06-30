package com.example.aicode.dto.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public record LoginResponse(

        String accessToken,

        String refreshToken,

        Instant expiresAt,

        UserResponse user

) {
}