package com.example.aicode.security.auth;

import com.example.aicode.domain.AuthProvider;
import com.example.aicode.domain.RoleType;
import com.example.aicode.domain.UserStatus;
import com.example.aicode.entity.User;
import com.example.aicode.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    private final DefaultOAuth2UserService delegate =
            new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oauth2User = delegate.loadUser(userRequest);

        AuthProvider provider = AuthProvider.valueOf(
                userRequest.getClientRegistration()
                        .getRegistrationId()
                        .toUpperCase()
        );

        OAuth2UserInfo userInfo =
                OAuth2UserInfoFactory.getOAuth2UserInfo(
                        provider,
                        oauth2User.getAttributes()
                );

        User user = userRepository
                .findByProviderAndProviderId(
                        provider,
                        userInfo.getId()
                )
                .map(existingUser ->
                        updateExistingUser(
                                existingUser,
                                provider,
                                userInfo
                        )
                )
                .orElseGet(() ->
                        createNewUser(
                                provider,
                                userInfo
                        )
                );

        return new CustomOAuth2User(
                user,
                oauth2User.getAttributes()
        );
    }

    private User createNewUser(
            AuthProvider provider,
            OAuth2UserInfo userInfo) {

        User user = User.builder()
                .firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName())
                .username(generateUsername(userInfo.getUsername()))
                .email(userInfo.getEmail())
                .provider(provider)
                .providerId(userInfo.getId())
                .role(RoleType.USER)
                .status(UserStatus.ACTIVE)
                .enabled(true)
                .emailVerified(userInfo.getEmail() != null)
                .build();

        return userRepository.save(user);
    }

    private User updateExistingUser(
            User user,
            AuthProvider provider,
            OAuth2UserInfo userInfo) {

        user.setProvider(provider);
        user.setProviderId(userInfo.getId());

        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            user.setFirstName(userInfo.getFirstName());
        }

        if (user.getLastName() == null || user.getLastName().isBlank()) {
            user.setLastName(userInfo.getLastName());
        }

        if (user.getEmail() == null && userInfo.getEmail() != null) {
            user.setEmail(userInfo.getEmail());
            user.setEmailVerified(true);
        }

        return userRepository.save(user);
    }

    private String generateUsername(String username) {

        String candidate = username;
        int counter = 1;

        while (userRepository.existsByUsername(candidate)) {
            candidate = username + counter++;
        }

        return candidate;
    }
}