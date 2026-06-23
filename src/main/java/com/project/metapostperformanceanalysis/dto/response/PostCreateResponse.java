package com.project.metapostperformanceanalysis.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record PostCreateResponse(
        Long id, String message, @JsonProperty("created_time") LocalDateTime createdTime
) {
}
