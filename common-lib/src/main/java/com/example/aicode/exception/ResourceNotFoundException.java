package com.example.aicode.exception;

import com.example.aicode.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String resource,
                                     Object identifier) {
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.RESOURCE_NOT_FOUND,
                String.format("%s with identifier '%s' was not found.",
                        resource,
                        identifier)
        );
    }
}