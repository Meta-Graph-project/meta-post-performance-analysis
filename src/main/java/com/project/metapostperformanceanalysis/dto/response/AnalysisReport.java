package com.project.metapostperformanceanalysis.dto.response;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public record AnalysisReport(List<PostResponse> top3EngagedPosts,
                             Map<DayOfWeek, Integer> likesByDayOfWeek,
                             DayOfWeek bestDay,
                             String summaryText) {
}
