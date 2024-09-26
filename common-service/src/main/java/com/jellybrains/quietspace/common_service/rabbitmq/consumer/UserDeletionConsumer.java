package com.jellybrains.quietspace.common_service.rabbitmq.consumer;

import com.jellybrains.quietspace.common_service.message.kafka.user.UserDeletionEvent;
import com.jellybrains.quietspace.common_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class UserDeletionConsumer {

    private final NotificationRepository repository;

    @RabbitListener(queues = "#{'${rabbitmq.queue.user.deletion}'}")
    public void deleteNotificationData(UserDeletionEvent event) {
        repository.deleteNotificationsByUserId(event.getUserId()).subscribe(
                signal -> log.info("notifications deleted for userId: {}", event.getUserId()),
                error -> log.info("notifications deletion failed cause: {}", error.getMessage())
        );
    }
}