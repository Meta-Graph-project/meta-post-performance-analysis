package com.project.metapostperformanceanalysis.mapper;

import com.project.metapostperformanceanalysis.entity.Post;
import com.project.metapostperformanceanalysis.dto.request.PostRequest;
import com.project.metapostperformanceanalysis.dto.response.PostResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toEntity(PostRequest request);

    List<PostResponse> toResponseList(List<Post> posts);

    PostResponse toResponse(Post post);


}
