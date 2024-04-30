package com.jellybrains.quietspace_backend_ms.notification_service;

import com.jellybrains.quietspace_backend_ms.notification_service.event.MessageSentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@SpringBootApplication
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    void handleNotification(MessageSentEvent messageSentEvent) {
        // TODO: implement a notification service
        log.info("Received message notification for chat: {}", messageSentEvent.getChatId());
    }

}
