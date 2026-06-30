package com.example.aicode.service.impl;

import com.example.aicode.domain.AuthProvider;
import com.example.aicode.domain.RoleType;
import com.example.aicode.domain.UserStatus;
import com.example.aicode.dto.request.LoginRequest;
import com.example.aicode.dto.request.RefreshTokenRequest;
import com.example.aicode.dto.request.RegisterRequest;
import com.example.aicode.dto.response.AuthenticationResponse;
import com.example.aicode.dto.response.LoginResponse;
import com.example.aicode.dto.response.UserResponse;
import com.example.aicode.entity.RefreshToken;
import com.example.aicode.entity.User;
import com.example.aicode.enums.ErrorCode;
import com.example.aicode.exception.DuplicateResourceException;
import com.example.aicode.exception.ResourceNotFoundException;
import com.example.aicode.mapper.UserMapper;
import com.example.aicode.repository.RefreshTokenRepository;
import com.example.aicode.repository.UserRepository;
import com.example.aicode.security.jwt.JwtService;
import com.example.aicode.service.AuthService;
import com.example.aicode.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {

        // Check duplicate email
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException(
                    ErrorCode.EMAIL_ALREADY_EXISTS,
                    "User",
                    "email",
                    request.email()
            );
        }

        // Check duplicate username
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException(
                    ErrorCode.USER_ALREADY_EXISTS,
                    "User",
                    "username",
                    request.username()
            );
        }

        // Create User
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(RoleType.USER)
                .enabled(true)
                .emailVerified(false)
                .provider(AuthProvider.LOCAL)
                .status(UserStatus.ACTIVE)
                .build();

        // Save user
        User savedUser = userRepository.save(user);

        // TODO: Generate JWT after JwtService is implemented
        String accessToken = jwtService.generateAccessToken(savedUser);

        String refreshToken = jwtService.generateRefreshToken(savedUser);

        return AuthenticationResponse.builder()
                .user(userMapper.toResponse(savedUser))
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpiresAt(jwtService.getRefreshTokenExpiry())
                .expiresAt(jwtService.getAccessTokenExpiry())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticationResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                request.email()
                        ));

        user.setLastLoginAt(LocalDateTime.now());

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .expiresAt(jwtService.getAccessTokenExpiry())
                .refreshToken(refreshToken)
                .refreshTokenExpiresAt(jwtService.getRefreshTokenExpiry())
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    @Transactional
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {

        // Validate refresh token
        RefreshToken refreshToken = refreshTokenService.verify(request.refreshToken());

        // Rotate refresh token
        RefreshToken newRefreshToken = refreshTokenService.rotate(refreshToken);

        User user = refreshToken.getUser();

        // Generate new access token
        String accessToken = jwtService.generateAccessToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken.getToken())
                .expiresAt(jwtService.getAccessTokenExpiry())
                .refreshTokenExpiresAt(newRefreshToken.getExpiresAt())
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    @Transactional
    public void logout(RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenService.verify(request.refreshToken());

        refreshToken.setRevoked(true);

        refreshTokenRepository.save(refreshToken);
    }
}