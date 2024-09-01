package com.jellybrains.quietspace.notification_service.repository;

import com.jellybrains.quietspace.common_service.enums.NotificationType;
import com.jellybrains.quietspace.notification_service.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    Page<Notification> findAllByUserId(String signedUserId, PageRequest pageRequest);

//    Page<Notification> findAllByUserId(String userId, Pageable pageable);

//    Page<Notification> findAllByUserIdAndNotificationType(String userId, NotificationType type, Pageable pageable);

//    Optional<Notification> findByContentIdAndUserId(String contentId, String userId);

//    Integer countByUserIdAndSeen(String userId, Boolean seen);

}
