package com.jellybrains.quietspace_backend_ms.notification_service.common.client;


import com.jellybrains.quietspace_backend_ms.notification_service.common.utils.enums.NotificationType;

public interface NotificationClient {

    void processNotification(NotificationType type, String contentId);

}
