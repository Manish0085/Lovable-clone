package com.example.aicode.dto.response;

import com.example.aicode.domain.UserStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponse(

        UUID id,

        String firstName,

        String lastName,

        String username,

        String email,

        UserStatus status,

        boolean emailVerified

) {
}