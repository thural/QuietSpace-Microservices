package com.jellybrains.quietspace_backend_ms.feedservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${GATEWAY_URL}")
    private String gatewayUrl;

    @Bean
    @LoadBalanced
    public WebClient webClient() {
        return WebClient.create(gatewayUrl);
    }
}
