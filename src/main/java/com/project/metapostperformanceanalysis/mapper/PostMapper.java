package com.project.metapostperformanceanalysis.mapper;

import com.project.metapostperformanceanalysis.dto.response.FeedItemResponse;
import com.project.metapostperformanceanalysis.entity.Post;
import com.project.metapostperformanceanalysis.dto.request.PostRequest;
import com.project.metapostperformanceanalysis.dto.response.PostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "metaPostId", source = "id")
    @Mapping(target = "likeCount", expression = "java(feedItemResponse.likes() != null ? feedItemResponse.likes().summary().count() : 0)")
    @Mapping(target = "commentCount", expression = "java(feedItemResponse.comments() != null ? feedItemResponse.comments().summary().count() : 0)")
    @Mapping(target = "createdTime",expression = "java(java.time.OffsetDateTime.parse(feedItemResponse.createdTime()).toLocalDateTime())")
    Post toEntity(FeedItemResponse feedItemResponse);

    List<PostResponse> toResponseList(List<Post> posts);

    PostResponse toResponse(Post post);


}
