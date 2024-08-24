package com.jellybrains.quietspace_backend_ms.feedservice.common.service;

import com.jellybrains.quietspace_backend_ms.feedservice.common.utils.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationService {

    public void processNotification(NotificationType type, String contentId){

    }
}
