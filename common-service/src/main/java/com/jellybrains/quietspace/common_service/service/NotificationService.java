package com.jellybrains.quietspace.common_service.service;

import com.jellybrains.quietspace.common_service.document.Notification;
import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.NotificationType;
import org.springframework.data.domain.Page;

public interface NotificationService {

    void handleSeen(String notificationId);

    void processNotification(NotificationType type, String contentId);

    void processNotificationByReaction(ContentType type, String contentId);

    Page<Notification> getAllNotifications(Integer pageNumber, Integer pageSize);

    Page<Notification> getNotificationsByType(Integer pageNumber, Integer pageSize, String notificationType);

    Integer getCountOfPendingNotifications();

}
