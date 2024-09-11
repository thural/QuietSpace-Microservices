package com.jellybrains.quietspace.common_service.repository;

import com.jellybrains.quietspace.common_service.document.Notification;
import com.jellybrains.quietspace.common_service.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    Page<Notification> findAllByUserId(String signedUserId, PageRequest pageRequest);

    void deleteNotificationsByUserId(String userId);

    Page<Notification> findAllByUserId(String userId, Pageable pageable);

    Page<Notification> findAllByUserIdAndNotificationType(String userId, NotificationType type, Pageable pageable);

    Optional<Notification> findByContentIdAndUserId(String contentId, String userId);

    Integer countByUserIdAndSeen(String userId, Boolean seen);

}
