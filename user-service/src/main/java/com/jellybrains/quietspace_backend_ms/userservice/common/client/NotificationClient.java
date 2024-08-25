package com.jellybrains.quietspace_backend_ms.userservice.common.client;


import com.jellybrains.quietspace_backend_ms.userservice.common.enums.ContentType;
import com.jellybrains.quietspace_backend_ms.userservice.common.enums.NotificationType;

public interface NotificationClient {

    void processNotification(NotificationType type, String contentId);

    void processNotificationByReaction(ContentType type, String contentId);

}
