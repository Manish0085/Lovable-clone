package com.example.aicode.mapper;

import com.example.aicode.dto.request.RegisterRequest;
import com.example.aicode.dto.response.UserResponse;
import com.example.aicode.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "providerId", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    User toEntity(RegisterRequest request);
}