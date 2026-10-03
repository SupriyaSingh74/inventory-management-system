package com.inventory.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorResponse {

    private boolean success;

    private String message;

    private int status;

    private Map<String, String> errors;

    private LocalDateTime timestamp;

}