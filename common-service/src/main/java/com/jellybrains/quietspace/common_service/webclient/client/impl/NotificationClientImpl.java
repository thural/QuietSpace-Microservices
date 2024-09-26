package com.jellybrains.quietspace.common_service.webclient.client.impl;

import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.NotificationType;
import com.jellybrains.quietspace.common_service.webclient.client.NotificationClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
@RequiredArgsConstructor
public class NotificationClientImpl implements NotificationClient {

    private final WebClient webClient;
    private final String NOTIFICATION_API_URI = "/api/v1/notifications/";

    @Override
    @CircuitBreaker(name = "common-service",
            fallbackMethod = "com.jellybrains.quietspace.common_service.service.shared.FallbackService#genericFallback")
    public void processNotification(NotificationType type, String contentId) {
        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(NOTIFICATION_API_URI + "process/")
                        .queryParam("contentId", contentId)
                        .queryParam("type", type)
                        .build()
                )
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    @CircuitBreaker(name = "common-service",
            fallbackMethod = "com.jellybrains.quietspace.common_service.service.shared.FallbackService#genericFallback")
    public void processNotificationByReaction(ContentType type, String contentId) {
        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(NOTIFICATION_API_URI + "process-reaction/")
                        .queryParam("contentId", contentId)
                        .queryParam("type", type)
                        .build()
                )
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
