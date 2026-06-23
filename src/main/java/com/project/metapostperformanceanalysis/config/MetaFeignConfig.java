package com.project.metapostperformanceanalysis.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class MetaFeignConfig {
    @Value(value = "${META_PAGE_ACCESS_TOKEN}")
    private String accessToken;

    @Value(value = "${META_APP_PAGE_ID}")
    private String pageId;


    @Bean
    public RequestInterceptor interceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                requestTemplate.query("access_token", accessToken);
                requestTemplate.header("pageId",pageId);
            }
        };


    }

}
