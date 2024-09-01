package com.jellybrains.quietspace.notification_service.service.impl;

import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.EventType;
import com.jellybrains.quietspace.common_service.enums.NotificationType;
import com.jellybrains.quietspace.common_service.message.websocket.NotificationEvent;
import com.jellybrains.quietspace.notification_service.exception.CustomNotFoundException;
import com.jellybrains.quietspace.notification_service.model.Notification;
import com.jellybrains.quietspace.notification_service.repository.NotificationRepository;
import com.jellybrains.quietspace.notification_service.service.NotificationService;
import com.jellybrains.quietspace.notification_service.webclient.service.CommentService;
import com.jellybrains.quietspace.notification_service.webclient.service.PostService;
import com.jellybrains.quietspace.notification_service.webclient.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import static com.jellybrains.quietspace.common_service.utils.PagingProvider.DEFAULT_SORT_OPTION;
import static com.jellybrains.quietspace.common_service.utils.PagingProvider.buildPageRequest;
import static com.jellybrains.quietspace.notification_service.controller.NotificationController.NOTIFICATION_EVENT_PATH;
import static com.jellybrains.quietspace.notification_service.controller.NotificationController.NOTIFICATION_SUBJECT_PATH;

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
    public void handleSeen(String notificationId) {
        log.info("setting notification with id {} as seen ...", notificationId);
        String userId = userService.getAuthorizedUserId();
        var notification = notificationRepository.findById(notificationId)
                .orElseThrow(CustomNotFoundException::new);

        if (!notification.getUserId().equals(userId))
            throw new ResourceAccessException("user is denied access for requested resource");

        if (!notification.getSeen()) {
            notification.setSeen(true);
            notificationRepository.save(notification);
        }

        var event = NotificationEvent.builder()
                .actorId(userId)
                .notificationId(notificationId)
                .type(EventType.SEEN_NOTIFICATION)
                .build();

        template.convertAndSendToUser(userId, NOTIFICATION_EVENT_PATH, event);
    }

    @Override
    public Page<Notification> getAllNotifications(Integer pageNumber, Integer pageSize) {
        String signedUserId = userService.getAuthorizedUserId();
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, DEFAULT_SORT_OPTION);
        return notificationRepository.findAllByUserId(signedUserId, pageRequest);
//        return Page.empty();
    }

    @Override
    public Page<Notification> getNotificationsByType(Integer pageNumber, Integer pageSize, String notificationType) {
        NotificationType type = NotificationType.valueOf(notificationType);
        String signedUserId = userService.getAuthorizedUserId();
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, DEFAULT_SORT_OPTION);
//        return notificationRepository.findAllByUserIdAndNotificationType(signedUserId, type, pageRequest);
        return Page.empty();
    }

    @Override
    public Integer getCountOfPendingNotifications() {
        String signedUserId = userService.getAuthorizedUserId();
//        return notificationRepository.countByUserIdAndSeen(signedUserId, false);
        return 0;
    }

    public void processNotification(NotificationType type, String contentId) {
        String signedUserId = userService.getAuthorizedUserId();
        String recipientId = getRecipientId(type, contentId);

        var notification = notificationRepository.save(
                Notification
                        .builder()
                        .notificationType(type)
                        .contentId(contentId)
                        .actorId(signedUserId)
                        .userId(recipientId)
                        .build()
        );

        try {
            log.info("notified {} user {}", notification.getNotificationType(), notification.getActorId());
            template.convertAndSendToUser(recipientId, NOTIFICATION_SUBJECT_PATH, notification);
        } catch (MessagingException exception) {
            log.info("failed to notify {} user {}", notification.getNotificationType(), notification.getActorId());
        }
    }

    public void processNotificationByReaction(ContentType type, String contentId) {
        switch (type) {
            case COMMENT -> processNotification(NotificationType.COMMENT_REACTION, contentId);
            case POST -> processNotification(NotificationType.POST_REACTION, contentId);
        }
    }

    private String getRecipientId(NotificationType type, String contentId) {
        return switch (type) {
            case COMMENT, REPOST, POST_REACTION -> postService.getUserIdByPostId(contentId);
            case COMMENT_REPLY, COMMENT_REACTION -> commentService.getUserIdByCommentId(contentId);
            case FOLLOW_REQUEST -> contentId;
        };
    }

}
