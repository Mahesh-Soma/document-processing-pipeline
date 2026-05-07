package com.mahesh.document_processing_pipeline.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
@Configuration
public class PipelineConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
