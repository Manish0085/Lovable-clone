package com.example.aicode.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldErrorResponse {

    private String field;

    private String message;
}