package com.project.metapostperformanceanalysis.mapper;

import com.project.metapostperformanceanalysis.domain.entity.Post;
import com.project.metapostperformanceanalysis.dto.request.PostRequest;
import com.project.metapostperformanceanalysis.dto.response.PostResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toEntity(PostRequest request);

    PostResponse toResponse(Post post);
}
