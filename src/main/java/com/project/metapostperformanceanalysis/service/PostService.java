package com.project.metapostperformanceanalysis.service;

import com.project.metapostperformanceanalysis.dto.response.AnalysisReportResponse;
import com.project.metapostperformanceanalysis.dto.response.PostResponse;
import com.project.metapostperformanceanalysis.entity.Post;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public interface PostService {
    List<Post> syncLatestPosts();

    List<PostResponse> getAllPosts();

    PostResponse getPostById(Long id);

    List<PostResponse> getTopEngagementPosts(Integer limit);

    Map<DayOfWeek, Integer> getLikesByDayOfWeek();

    DayOfWeek getBestPerformingDay();

    AnalysisReportResponse buildAnalysisReport();

}
