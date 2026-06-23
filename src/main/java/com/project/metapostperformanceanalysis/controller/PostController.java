package com.project.metapostperformanceanalysis.controller;

import com.project.metapostperformanceanalysis.dto.response.AnalysisReportResponse;
import com.project.metapostperformanceanalysis.dto.response.PostResponse;
import com.project.metapostperformanceanalysis.entity.Post;
import com.project.metapostperformanceanalysis.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponse> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostResponse getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @GetMapping("/best-day")
    @ResponseStatus(HttpStatus.OK)
    public DayOfWeek getBestPerformingDay() {
        return postService.getBestPerformingDay();
    }

    @PostMapping("/sync/post")
    @ResponseStatus(HttpStatus.OK)
    public List<Post> syncLatestPosts() {
        return postService.syncLatestPosts();
    }

    @GetMapping("/likes-by-day")
    @ResponseStatus(HttpStatus.OK)
    public Map<DayOfWeek, Integer> getLikesByDayOfWeek() {
        return postService.getLikesByDayOfWeek();
    }

    @GetMapping("/top")
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponse> getTopEngagementPosts(
            @RequestParam(defaultValue = "3") Integer limit) {
        return postService.getTopEngagementPosts(limit);
    }
    @GetMapping("/analysis")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisReportResponse buildAnalysisReport() {
        return postService.buildAnalysisReport();
    }
}
