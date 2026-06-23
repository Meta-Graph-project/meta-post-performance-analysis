package com.project.metapostperformanceanalysis.dto.response;

import lombok.Builder;



@Builder
public record ErrorResponse (
         int status,
         String error,
         String message
){

}
