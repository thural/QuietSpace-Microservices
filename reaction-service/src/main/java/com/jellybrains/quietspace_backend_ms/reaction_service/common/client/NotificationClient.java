package com.jellybrains.quietspace_backend_ms.reaction_service.common.client;


import com.jellybrains.quietspace_backend_ms.reaction_service.common.enums.ContentType;
import com.jellybrains.quietspace_backend_ms.reaction_service.common.enums.NotificationType;

public interface NotificationClient {

    void processNotification(NotificationType type, String contentId);

    void processNotificationByReaction(ContentType type, String contentId);

}
