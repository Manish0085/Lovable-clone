package com.example.aicode.exception;

import org.springframework.http.HttpStatus;


import com.example.aicode.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends ApiException {

    public ForbiddenException(String message) {
        super(
                HttpStatus.FORBIDDEN,
                ErrorCode.ACCESS_DENIED,
                message
        );
    }
}