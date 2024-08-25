package com.jellybrains.quietspace_backend_ms.notification_service.repository;

import com.jellybrains.quietspace.common_service.enums.NotificationType;
import com.jellybrains.quietspace_backend_ms.notification_service.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    Page<Notification> findAllByUserId(String userId, Pageable pageable);

    Page<Notification> findAllByUserIdAndNotificationType(String userId, NotificationType type, Pageable pageable);

    Optional<Notification> findByContentIdAndUserId(String contentId, String id);

    Integer countByUserIdAndIsSeen(String contentId, Boolean isSeen);

}
