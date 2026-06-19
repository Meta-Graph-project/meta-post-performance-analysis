package com.project.metapostperformanceanalysis.dto.response;

import com.project.metapostperformanceanalysis.domain.enums.TokenType;

import java.time.LocalDateTime;

public record TokenResponse(
        Long id,
        String accessToken,
        TokenType tokenType,
        LocalDateTime expiresAt,
        LocalDateTime createdAt
) {
}
