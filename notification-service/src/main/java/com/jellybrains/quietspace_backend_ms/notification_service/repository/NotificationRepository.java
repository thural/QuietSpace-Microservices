package com.jellybrains.quietspace_backend_ms.notification_service.repository;

import com.jellybrains.quietspace_backend_ms.notification_service.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
