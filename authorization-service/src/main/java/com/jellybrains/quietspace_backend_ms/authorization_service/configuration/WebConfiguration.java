package com.jellybrains.quietspace_backend_ms.authorization_service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfiguration {

    @Value("${gateway.url}")
    private String gatewayUrl;


    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(gatewayUrl)
                .filter(new ServletBearerExchangeFilterFunction())
                .build();
    }
}

