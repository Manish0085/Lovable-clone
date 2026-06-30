package com.example.aicode.exception;

import com.example.aicode.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException {

    public UnauthorizedException(String message) {
        super(
                HttpStatus.UNAUTHORIZED,
                ErrorCode.UNAUTHORIZED,
                message
        );
    }
}