package com.jellybrains.quietspace.reaction_service.rabbitmq.consumer;

import com.jellybrains.quietspace.common_service.message.kafka.user.UserDeletionEvent;
import com.jellybrains.quietspace.reaction_service.repository.ReactionRepository;
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

    private final ReactionRepository repository;

    @RabbitListener(queues = "#{'${rabbitmq.queue.user.deletion}'}")
    public void deleteReactionData(UserDeletionEvent event) {
        repository.deleteReactionsByUserId(event.getUserId())
                .subscribe(
                        signal -> log.info("reactions deleted for userId: {}", event.getUserId()),
                        error -> log.info("reactions deletion failed for userId: {} cause: {}",
                                event.getUserId(), error.getMessage())
                );
    }
}