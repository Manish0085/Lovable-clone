package com.example.aicode.exception;

import com.example.aicode.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class BusinessException extends ApiException {

    public BusinessException(ErrorCode errorCode,
                             String message) {
        super(HttpStatus.BAD_REQUEST, errorCode, message);
    }
}