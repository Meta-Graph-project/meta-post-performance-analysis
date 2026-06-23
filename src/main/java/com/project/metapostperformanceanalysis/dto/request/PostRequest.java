package com.project.metapostperformanceanalysis.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;


public record PostRequest(
        @NotBlank(message = "Message cannot be blank")
        @Size(max = 500, message = "Message can be maximum 500 characters")
        String message,
        @NotNull(message = "Created time cannot be null")
        @PastOrPresent(message = "Created time must be in the past or present")
        LocalDateTime createdTime,
        @NotNull(message = "Like count cannot be null")
        @PositiveOrZero(message = "Like count cannot be negative")
        Integer likeCount,
        @NotNull(message = "Comment count cannot be null")
        @PositiveOrZero(message = "Comment count cannot be negative")
        Integer commentCount) {

}
