package com.jellybrains.quietspace_backend_ms.feedservice.common.client;

import com.jellybrains.quietspace_backend_ms.feedservice.common.utils.enums.NotificationType;


public interface NotificationClient {

    void processNotification(NotificationType type, String contentId);

}
