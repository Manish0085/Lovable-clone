package com.example.aicode.security.auth;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class OAuth2UserInfo {

    protected final Map<String, Object> attributes;

    public abstract String getUsername();

    protected OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();

    public abstract String getEmail();

    public abstract String getFirstName();

    public abstract String getLastName();

    public abstract String getFullName();
}