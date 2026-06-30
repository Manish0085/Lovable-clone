package com.example.aicode.service;


import com.example.aicode.dto.request.LoginRequest;
import com.example.aicode.dto.request.RefreshTokenRequest;
import com.example.aicode.dto.request.RegisterRequest;
import com.example.aicode.dto.response.AuthenticationResponse;
import com.example.aicode.dto.response.LoginResponse;
import com.example.aicode.dto.response.UserResponse;
public interface AuthService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);

    AuthenticationResponse refreshToken(RefreshTokenRequest request);

    void logout(RefreshTokenRequest request);

}