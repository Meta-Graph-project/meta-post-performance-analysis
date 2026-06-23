package com.project.metapostperformanceanalysis.dto.response;

import java.util.List;

public record FeedResponse(
        List<FeedItemResponse> data,
        MetaPagingResponse paging
) {
}
