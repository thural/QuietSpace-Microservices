package com.jellybrains.quietspace_backend_ms.notification_service.common.client.impl;

import com.jellybrains.quietspace_backend_ms.notification_service.common.client.NotificationClient;
import com.jellybrains.quietspace_backend_ms.notification_service.common.utils.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class NotificationClientImpl implements NotificationClient {

    private final WebClient webClient;
    private final String NOTIFICATION_API_URI = "/api/v1/notifications/";

    @Override
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
}
