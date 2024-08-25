package com.jellybrains.quietspace.common_service.client;


import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.NotificationType;

public interface NotificationClient {

    void processNotification(NotificationType type, String contentId);

    void processNotificationByReaction(ContentType type, String contentId);

}
