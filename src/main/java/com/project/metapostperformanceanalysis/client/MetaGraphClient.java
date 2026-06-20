package com.project.metapostperformanceanalysis.client;

import com.project.metapostperformanceanalysis.config.MetaFeignConfig;
import com.project.metapostperformanceanalysis.dto.response.FeedResponse;
import com.project.metapostperformanceanalysis.dto.response.PostCreateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "metaGraphClient", url = "https://graph.facebook.com/v25.0", configuration = MetaFeignConfig.class)
public interface MetaGraphClient {

    @GetMapping("/{pageId}/feed")
    FeedResponse getFeed(
            @PathVariable("pageId") String pageId,
            @RequestParam("fields") String fields,
            @RequestParam("limit") int limit
    );

    @PostMapping("{pageId}/feed")
    PostCreateResponse createPost(
            @PathVariable("pageId") String pageId,
            @RequestParam("message") String message,
            @RequestParam("fields") String fields

    );
}
