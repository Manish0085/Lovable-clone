package com.example.aicode.exception;

import com.example.aicode.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends ApiException {

    public DuplicateResourceException(ErrorCode errorCode,
                                      String resource,
                                      String field,
                                      Object value) {

        super(
                HttpStatus.CONFLICT,
                errorCode,
                String.format(
                        "%s already exists with %s '%s'.",
                        resource,
                        field,
                        value
                )
        );
    }
}