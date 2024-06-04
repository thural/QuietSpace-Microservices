package com.jellybrains.quietspace_backend_ms.reaction_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${values.gateway.url}")
    private String gatewayUrl;

    @Bean
    @LoadBalanced
    public WebClient webClient() {
        return WebClient.create(gatewayUrl);
    }

}
