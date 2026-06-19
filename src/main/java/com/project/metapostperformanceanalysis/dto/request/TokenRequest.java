package com.project.metapostperformanceanalysis.dto.request;

import com.project.metapostperformanceanalysis.domain.enums.TokenType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record TokenRequest(

        @NotNull(message = "Access token cannot be null")
        @Size(min = 10, message = "Access token is too short")
        String accessToken,

        @NotNull(message = "Token type cannot be null")
        TokenType tokenType,

        @NotNull(message = "Expiration date cannot be null")
        @Future(message = "Expiration date must be in the future")
        LocalDateTime expiresAt,

        @NotNull(message = "Created date cannot be null")
        LocalDateTime createdAt
) {
}
