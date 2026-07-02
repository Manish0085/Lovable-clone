package com.example.aicode.security.auth;

import com.example.aicode.domain.AuthProvider;

import java.util.Map;

public final class OAuth2UserInfoFactory {

    private OAuth2UserInfoFactory() {
    }

    public static OAuth2UserInfo getOAuth2UserInfo(
            AuthProvider provider,
            Map<String, Object> attributes) {

        return switch (provider) {

            case GOOGLE -> new GoogleOAuth2UserInfo(attributes);

            case GITHUB -> new GithubOAuth2UserInfo(attributes);

            default -> throw new IllegalArgumentException(
                    "Unsupported OAuth Provider : " + provider);
        };
    }
}