package com.project.metapostperformanceanalysis.dto.response;

import java.time.LocalDateTime;


public record PostResponse(Long id,
                           String message,
                           LocalDateTime createdTime,
                           Integer likeCount,
                           Integer commentCount,
                           Integer engagement) {


}
