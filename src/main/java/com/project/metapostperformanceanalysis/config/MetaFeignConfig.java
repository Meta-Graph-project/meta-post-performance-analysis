package com.project.metapostperformanceanalysis.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class MetaFeignConfig {
    @Value(value = "${meta.api.access-token}")
    private String accessToken;

    @Bean
    public RequestInterceptor interceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                requestTemplate.query("access_token", accessToken);
            }
        };
    }

}
