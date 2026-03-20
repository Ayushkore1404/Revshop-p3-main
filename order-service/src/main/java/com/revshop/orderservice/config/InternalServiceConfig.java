package com.revshop.orderservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InternalServiceConfig {

    @Bean
    public RequestInterceptor internalServiceRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // Add internal service token for service-to-service communication
                template.header("X-Internal-Service-Token", "internal-service-key");
                template.header("Content-Type", "application/json");
            }
        };
    }
}
