package com.project.metapostperformanceanalysis.service.serviceImpl;

import com.project.metapostperformanceanalysis.client.MetaGraphClient;
import com.project.metapostperformanceanalysis.config.MetaFeignConfig;
import com.project.metapostperformanceanalysis.dto.response.AnalysisReportResponse;
import com.project.metapostperformanceanalysis.dto.response.FeedItemResponse;
import com.project.metapostperformanceanalysis.dto.response.FeedResponse;
import com.project.metapostperformanceanalysis.dto.response.PostResponse;
import com.project.metapostperformanceanalysis.entity.Post;
import com.project.metapostperformanceanalysis.exception.NotFoundException;
import com.project.metapostperformanceanalysis.mapper.PostMapper;
import com.project.metapostperformanceanalysis.repository.PostRepository;
import com.project.metapostperformanceanalysis.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper mapper;
    private final MetaGraphClient metaGraphClient;
    private final MetaFeignConfig config;


    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Id not found" + ":" + id));
        log.info("successfully found ID" + ":" + id);
        return mapper.toResponse(post);
    }


    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        log.info("successfully returned all posts");
        return mapper.toResponseList(posts);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("bestDay")
    public DayOfWeek getBestPerformingDay() {
        Map<DayOfWeek, Integer> likesByDay = getLikesByDayOfWeek();
        if (likesByDay.isEmpty()) {
            throw new NotFoundException("Post not found");

        }
        return likesByDay.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElseThrow(() -> new NotFoundException("Best day not found"));
    }

    @Override
    @Transactional
    @Cacheable("likesByDay")
    public Map<DayOfWeek, Integer> getLikesByDayOfWeek() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().collect(Collectors.groupingBy(
                post -> post.getCreatedTime().getDayOfWeek(), Collectors.summingInt(Post::getLikeCount)
        ));
    }


    @Override
    @CacheEvict(value = {"topPosts", "likesByDay", "bestDay", "analysisReport"}, allEntries = true)
    @Transactional
    public List<Post> syncLatestPosts() {
        String fields = "id,message,created_time,permalink_url,likes.summary(true),comments.summary(true)";
        FeedResponse feedResponse = metaGraphClient.getFeed(config.getPageId(), fields, 5);
        if (feedResponse == null || feedResponse.data().isEmpty()) {
            return List.of();
        }
        return feedResponse.data().stream().map(this::mapToPost).toList();

    }

    private Post mapToPost(FeedItemResponse feedItemResponse) {
        Post available = postRepository.findByMetaPostId(feedItemResponse.id())
                .orElseGet(Post::new);

        available.setMetaPostId(feedItemResponse.id());
        available.setMessage(feedItemResponse.message());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        available.setCreatedTime(LocalDateTime.parse(feedItemResponse.createdTime(), formatter));
        available.setLikeCount(feedItemResponse.likes() != null ? feedItemResponse.likes().summary().count() : 0);
        available.setCommentCount(feedItemResponse.comments() != null ? feedItemResponse.comments().summary().count() : 0);

        return postRepository.save(available);
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable("topPosts")
    public List<PostResponse> getTopEngagementPosts(Integer limit) {
        return postRepository.findTopByEngagement(PageRequest.of(0, limit))
                .stream()
                .map(mapper::toResponse)
                .toList();

    }


    @Override
    @Cacheable("analysisReport")
    @Transactional(readOnly = true)
    public AnalysisReportResponse buildAnalysisReport() {
        List<PostResponse> topThree = getTopEngagementPosts(3);
        Map<DayOfWeek, Integer> likesByDay = getLikesByDayOfWeek();
        DayOfWeek bestDay = getBestPerformingDay();

        String summary = "The highest-engagement posts were identified. Posts tend to get more likes on: %s".formatted(bestDay);

        return new AnalysisReportResponse(topThree, likesByDay, bestDay, summary);
    }
}


