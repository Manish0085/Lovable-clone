package com.example.aicode.service;

import com.example.aicode.entity.RefreshToken;
import com.example.aicode.entity.User;

public interface RefreshTokenService {

    RefreshToken create(User user, String token);

    RefreshToken verify(String token);

    RefreshToken rotate(RefreshToken oldToken);

    void revokeAll(User user);

    void saveOrUpdate(User user, String refreshToken);
}