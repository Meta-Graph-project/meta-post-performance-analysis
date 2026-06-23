package com.project.metapostperformanceanalysis.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record FeedItemResponse(
        String id, String message, @JsonProperty("created_time") String createdTime,
        @JsonProperty("permalink_url") String permaUrl,
        Likes likes, Comments comments
) {
    public record Likes(Summary summary
    ) {
    }

    public record Comments(Summary summary) {
    }

    public record Summary(
            @JsonProperty("total_count") Integer count
    ) {
    }
}
