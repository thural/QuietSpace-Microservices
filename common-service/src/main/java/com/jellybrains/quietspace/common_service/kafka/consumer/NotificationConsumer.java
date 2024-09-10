package com.jellybrains.quietspace.common_service.kafka.consumer;

import com.jellybrains.quietspace.common_service.message.kafka.notification.NotificationEvent;
import com.jellybrains.quietspace.common_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService service;

    @KafkaListener(topics = "#{'${kafka.topics.notification}'}")
    public void process(NotificationEvent event) {
        try {
            log.info("processing notification event for userId: {}", event.getUserId());
            service.processNotification(event.getNotificationType(), event.getContentId());
        } catch (Exception e) {
            log.info("failed to process notification event, cause: {}", e.getMessage());
        }
    }

}