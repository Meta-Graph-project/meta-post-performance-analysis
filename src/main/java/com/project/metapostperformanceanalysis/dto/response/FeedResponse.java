package com.project.metapostperformanceanalysis.dto.response;

import lombok.Getter;

import java.util.List;

public record FeedResponse(
        List<FeedItemResponse> data,
        MetaPagingResponse paging
) {
}
