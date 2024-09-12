package com.jellybrains.quietspace.common_service.repository;

import com.jellybrains.quietspace.common_service.document.Notification;
import com.jellybrains.quietspace.common_service.enums.NotificationType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificationRepository extends ReactiveMongoRepository<Notification, String> {

    Flux<Notification> findAllByUserId(String signedUserId, PageRequest pageRequest);

    Mono<Void> deleteNotificationsByUserId(String userId);

    Flux<Notification> findAllByUserId(String userId, Pageable pageable);

    Flux<Notification> findAllByUserIdAndNotificationType(String userId, NotificationType type, Pageable pageable);

    Mono<Notification> findByContentIdAndUserId(String contentId, String userId);

    Mono<Integer> countByUserIdAndSeen(String userId, Boolean seen);

}
