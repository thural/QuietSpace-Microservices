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
        service.processNotification(event.getNotificationType(), event.getContentId())
                .subscribe(
                        signal -> log.info("processed notification event for userId: {}", event.getUserId()),
                        error -> log.info("failed to process notification event, cause: {}", error.getMessage())
                );
    }

}