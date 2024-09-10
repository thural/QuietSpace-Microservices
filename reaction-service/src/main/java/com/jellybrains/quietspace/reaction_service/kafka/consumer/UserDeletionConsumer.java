package com.jellybrains.quietspace.reaction_service.kafka.consumer;

import com.jellybrains.quietspace.common_service.message.kafka.user.UserDeletionEvent;
import com.jellybrains.quietspace.reaction_service.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class UserDeletionConsumer {

    private final ReactionRepository repository;

    @KafkaListener(topics = "#{'${kafka.topics.user.deletion}'}")
    public void deleteReactionData(UserDeletionEvent event) {
        try {
            repository.deleteReactionsByUserId(event.getUserId());
            log.info("reactions deleted for userId: {}", event.getUserId());
        } catch (Exception e) {
            log.info("reactions deletion failed for userId: {} cause: {}", event.getUserId(), e.getMessage());
        }
    }

}