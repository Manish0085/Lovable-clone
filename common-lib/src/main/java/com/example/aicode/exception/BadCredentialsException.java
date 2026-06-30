package com.example.aicode.exception;

import com.example.aicode.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class BadCredentialsException extends ApiException {

    public BadCredentialsException(String message) {
        super(
                HttpStatus.UNAUTHORIZED,
                ErrorCode.INVALID_CREDENTIALS,
                message
        );
    }
}