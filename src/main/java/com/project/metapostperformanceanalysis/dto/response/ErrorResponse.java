package com.project.metapostperformanceanalysis.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;



@Builder
public record ErrorResponse (
         int status,
         String error,
         String message
){

}
