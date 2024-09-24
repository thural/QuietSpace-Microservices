package com.jellybrains.quietspace.common_service.rabbitmq.producer;

import com.jellybrains.quietspace.common_service.message.kafka.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProducer {

    @Value("${rabbitmq.topic.user}")
    String userTopicExchange;

    @Value("$[rabbitmq.queue.user.creation}")
    String userCreation;

    @Value("$[rabbitmq.queue.user.creation-failed}")
    String userCreationFailed;

    @Value("$[rabbitmq.queue.user.deletion}")
    String userDeletion;

    @Value("$[rabbitmq.queue.user.deletion-failed}")
    String userDeletionFailed;

    @Value("$[rabbitmq.queue.user.update}")
    String userUpdate;

    @Value("$[rabbitmq.queue.user.update-failed}")
    String userUpdateFailed;

    private final RabbitTemplate rabbitTemplate;


    public void userCreation(UserCreationEvent event) {
        rabbitTemplate.convertAndSend(userTopicExchange, userCreation, event);
    }

    public void userCreationFailed(UserCreationEventFailed event) {
        rabbitTemplate.convertAndSend(userTopicExchange, userCreationFailed, event);
    }

    public void userDeletion(UserDeletionEvent event) {
        rabbitTemplate.convertAndSend(userTopicExchange, userDeletion, event);
    }

    public void userDeletionFailed(UserDeletionFailedEvent event) {
        rabbitTemplate.convertAndSend(userTopicExchange, userDeletionFailed, event);
    }

    public void userUpdateFailed(UserUpdateFailedEvent event) {
        rabbitTemplate.convertAndSend(userTopicExchange, userUpdate, event);
    }
}
