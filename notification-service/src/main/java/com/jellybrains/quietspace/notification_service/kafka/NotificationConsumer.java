package com.jellybrains.quietspace.notification_service.kafka;

import com.jellybrains.quietspace.common_service.enums.NotificationType;
import com.jellybrains.quietspace.notification_service.event.NewMessageEvent;
import com.jellybrains.quietspace.notification_service.model.Notification;
import com.jellybrains.quietspace.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
//@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final NotificationRepository notificationRepository;
    ;
    // TODO: implement a socket service

    @KafkaListener(topics = "notification-topic")
    public void consumeNewMessageNotification(NewMessageEvent newMessageEvent) {
        log.info("New message received: {}", newMessageEvent.getMessage());
        notificationRepository.save(
                Notification.builder()
                        .message(newMessageEvent.getMessage())
                        .notificationType(NotificationType.COMMENT)
                        .userId(newMessageEvent.getReceiverId())
                        .build()
        );
        // TODO: use a socket to relay notification to frontend client
    }
}
