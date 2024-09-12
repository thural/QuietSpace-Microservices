package com.jellybrains.quietspace.common_service.service;

import com.jellybrains.quietspace.common_service.document.Notification;
import com.jellybrains.quietspace.common_service.enums.NotificationType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificationService {

    Mono<Void> handleSeen(String notificationId);

    Mono<Void> processNotification(NotificationType type, String contentId);

    Flux<Notification> getAllNotifications(Integer pageNumber, Integer pageSize);

    Flux<Notification> getNotificationsByType(Integer pageNumber, Integer pageSize, String notificationType);

    Mono<Integer> getCountOfPendingNotifications();

}
