package com.jellybrains.quietspace.common_service.webclient.client;

import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.NotificationType;

import java.util.concurrent.CompletableFuture;

public interface NotificationClient {

    CompletableFuture<Void> processNotification(NotificationType type, String contentId);

    CompletableFuture<Void> processNotificationByReaction(ContentType type, String contentId);

}
