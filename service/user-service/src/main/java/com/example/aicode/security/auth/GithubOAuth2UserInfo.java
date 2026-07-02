package com.example.aicode.security.auth;

import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

    public GithubOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getUsername() {
        return (String) attributes.get("login");
    }
    
    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getFirstName() {

        String name = getFullName();

        if (name == null || name.isBlank()) {
            return "GitHub";
        }

        return name.split(" ")[0];
    }

    @Override
    public String getLastName() {

        String name = getFullName();

        if (name == null || name.isBlank()) {
            return "";
        }

        String[] split = name.split(" ", 2);

        return split.length > 1 ? split[1] : "";
    }

    @Override
    public String getFullName() {
        return (String) attributes.get("name");
    }
}