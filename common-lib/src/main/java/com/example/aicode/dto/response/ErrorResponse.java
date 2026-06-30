package com.example.aicode.dto.response;

import com.example.aicode.dto.response.FieldErrorResponse;
import com.example.aicode.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private boolean success;

    private int status;

    /**
     * HTTP reason phrase
     * Example: "Bad Request", "Conflict", "Not Found"
     */
    private String error;

    /**
     * Application specific error code
     * Example: USER_001, AUTH_002, PROJECT_003
     */
    private ErrorCode errorCode;

    /**
     * Human readable message
     */
    private String message;

    /**
     * API endpoint
     */
    private String path;

    /**
     * Validation errors (optional)
     */
    private List<FieldErrorResponse> errors;

    private Instant timestamp;
}