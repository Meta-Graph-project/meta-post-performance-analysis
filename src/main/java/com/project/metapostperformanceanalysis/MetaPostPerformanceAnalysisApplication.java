package com.project.metapostperformanceanalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MetaPostPerformanceAnalysisApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetaPostPerformanceAnalysisApplication.class, args);
    }


}
