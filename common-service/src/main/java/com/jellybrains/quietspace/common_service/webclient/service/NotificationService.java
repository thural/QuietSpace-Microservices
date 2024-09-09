package com.jellybrains.quietspace.common_service.webclient.service;

import com.jellybrains.quietspace.common_service.enums.NotificationType;
import com.jellybrains.quietspace.common_service.webclient.client.NotificationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationClient notificationClient;

    public void processNotification(NotificationType type, String contentId) {
        try {
            notificationClient.processNotification(type, contentId);
        } catch (Exception e) {
            log.info("failed to process notification for contentId: {} due to: {}", contentId, e.getMessage());
        }
    }
}
