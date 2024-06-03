package com.jellybrains.quietspace_backend_ms.notification_service.kafka;

import com.jellybrains.quietspace_backend_ms.notification_service.event.NewMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    // TODO: implement notification repository
    // TODO: implement a socket service

    @KafkaListener(topics = "notification-topic")
    public void consumeNewMessageNotification(NewMessageEvent newMessageEvent) {
        log.info("New message received: {}", newMessageEvent.getMessage());
        // TODO: build and save notification object to repository
        // TODO: use a socket to relay notification to frontend client
    }
}
