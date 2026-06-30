package com.example.aicode.controller;

import com.example.aicode.dto.request.LoginRequest;
import com.example.aicode.dto.request.RefreshTokenRequest;
import com.example.aicode.dto.request.RegisterRequest;
import com.example.aicode.dto.response.ApiResponse;
import com.example.aicode.dto.response.AuthenticationResponse;
import com.example.aicode.dto.response.UserResponse;
import com.example.aicode.mapper.UserMapper;
import com.example.aicode.security.CustomUserDetails;
import com.example.aicode.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(authService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(authService.login(request))
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(authService.refreshToken(request))
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me(
            Authentication authentication) {

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(
                ApiResponse.success(
                        userMapper.toResponse(userDetails.getUser())
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody RefreshTokenRequest request) {

        authService.logout(request);

        return ResponseEntity.ok(
                ApiResponse.success(null)
        );
    }
}