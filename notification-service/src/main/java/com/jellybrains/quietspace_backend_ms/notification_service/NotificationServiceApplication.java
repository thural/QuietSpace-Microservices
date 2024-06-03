package com.jellybrains.quietspace_backend_ms.notification_service;

import com.jellybrains.quietspace_backend_ms.notification_service.event.NewMessageEvent;
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

    @KafkaListener(topics = "notification-topic")
    void handleNotification(NewMessageEvent newMessageEvent) {
        // TODO: implement a notification service
        log.info("received message notification for chat: {}", newMessageEvent.getChatId());
    }

}
