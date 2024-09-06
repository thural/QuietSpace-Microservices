package com.jellybrains.quietspace.common_service.kafka.consumer;

import com.jellybrains.quietspace.common_service.message.kafka.user.UserCreationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserConsumer {

    @KafkaListener(topics = "#{'${kafka.topics.user}'}")
    public void logUserCreationEvent(UserCreationEvent event) {
        log.info("consumed a user creation event with body: {}", event.getEventBody());
    }

}