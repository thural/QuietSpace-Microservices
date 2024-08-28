package com.jellybrains.quietspace.notification_service.webclient.service;

import com.jellybrains.quietspace.common_service.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationService {

    public void processNotification(NotificationType type, String contentId){

    }
}
