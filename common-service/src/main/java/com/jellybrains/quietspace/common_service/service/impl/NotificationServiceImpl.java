package com.jellybrains.quietspace.common_service.service.impl;

import com.jellybrains.quietspace.common_service.document.Notification;
import com.jellybrains.quietspace.common_service.enums.EventType;
import com.jellybrains.quietspace.common_service.enums.NotificationType;
import com.jellybrains.quietspace.common_service.exception.CustomNotFoundException;
import com.jellybrains.quietspace.common_service.message.websocket.NotificationEvent;
import com.jellybrains.quietspace.common_service.repository.NotificationRepository;
import com.jellybrains.quietspace.common_service.service.NotificationService;
import com.jellybrains.quietspace.common_service.service.shared.CommentService;
import com.jellybrains.quietspace.common_service.service.shared.PostService;
import com.jellybrains.quietspace.common_service.service.shared.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static com.jellybrains.quietspace.common_service.controller.NotificationController.NOTIFICATION_EVENT_PATH;
import static com.jellybrains.quietspace.common_service.controller.NotificationController.NOTIFICATION_SUBJECT_PATH;
import static com.jellybrains.quietspace.common_service.utils.PagingProvider.DEFAULT_SORT_OPTION;
import static com.jellybrains.quietspace.common_service.utils.PagingProvider.buildPageRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final SimpMessagingTemplate template;
    private final PostService postService;
    private final CommentService commentService;

    @Override
    public Mono<Void> handleSeen(String notificationId) {
        log.info("setting notification with id {} as seen ...", notificationId);
        String userId = userService.getAuthorizedUserId();

        return notificationRepository.findById(notificationId)
                .switchIfEmpty(Mono.error(CustomNotFoundException::new))
                .doOnNext(notification -> {
                    if (!notification.getSeen()) notification.setSeen(true);
                })
                .map(notification -> NotificationEvent.builder()
                        .actorId(userId)
                        .notificationId(notificationId)
                        .type(EventType.SEEN_NOTIFICATION).build()
                )
                .doOnNext(event -> template.convertAndSendToUser(userId, NOTIFICATION_EVENT_PATH, event))
                .onErrorContinue((error, message) -> {
                    if (error instanceof MessagingException)
                        log.info("failed to send seen event due to {}", message);
                })
                .then().doFinally(event -> log.info("set notification as seen and sent event:{}", event));
    }

    @Override
    public Flux<Notification> getAllNotifications(Integer pageNumber, Integer pageSize) {
        String signedUserId = userService.getAuthorizedUserId();
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, DEFAULT_SORT_OPTION);
        return notificationRepository.findAllByUserId(signedUserId, pageRequest);
    }

    @Override
    public Flux<Notification> getNotificationsByType(Integer pageNumber, Integer pageSize, String notificationType) {
        NotificationType type = NotificationType.valueOf(notificationType);
        String signedUserId = userService.getAuthorizedUserId();
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, DEFAULT_SORT_OPTION);
        return notificationRepository.findAllByUserIdAndNotificationType(signedUserId, type, pageRequest);
    }

    @Override
    public Mono<Integer> getCountOfPendingNotifications() {
        String signedUserId = userService.getAuthorizedUserId();
        return notificationRepository.countByUserIdAndSeen(signedUserId, false);
    }

    public Mono<Void> processNotification(NotificationType type, String contentId) {
        String signedUserId = userService.getAuthorizedUserId();
        String recipientId = getRecipientId(type, contentId).join();
        return notificationRepository.save(Notification.builder()
                .notificationType(type)
                .contentId(contentId)
                .actorId(signedUserId)
                .userId(recipientId).build()
        ).doOnNext(notification -> {
            log.info("notifying {} user {}", notification.getNotificationType(), recipientId);
            template.convertAndSendToUser(recipientId, NOTIFICATION_SUBJECT_PATH, notification);
        }).onErrorContinue((error, message) -> {
            if (error instanceof MessagingException)
                log.info("failed to notify user due to {}", message);
        }).then();
    }

    private CompletableFuture<String> getRecipientId(NotificationType type, String contentId) {
        return switch (type) {
            case COMMENT, REPOST, POST_REACTION -> postService.getUserIdByPostId(contentId);
            case COMMENT_REPLY, COMMENT_REACTION -> commentService.getUserIdByCommentId(contentId);
            case FOLLOW_REQUEST -> CompletableFuture.supplyAsync(() -> contentId);
        };
    }

}
