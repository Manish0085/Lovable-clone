package com.example.aicode.exception;

import com.example.aicode.dto.response.FieldErrorResponse;
import com.example.aicode.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ValidationException extends ApiException {

    private final List<FieldErrorResponse> errors;

    public ValidationException(List<FieldErrorResponse> errors) {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.VALIDATION_FAILED,
                "Validation failed."
        );
        this.errors = errors;
    }
}