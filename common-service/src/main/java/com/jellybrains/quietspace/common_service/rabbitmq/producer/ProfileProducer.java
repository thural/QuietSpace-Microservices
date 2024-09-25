package com.jellybrains.quietspace.common_service.rabbitmq.producer;

import com.jellybrains.quietspace.common_service.message.kafka.profile.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileProducer {

    @Value("${rabbitmq.topic.profile}")
    String profileTopicExchange;

    @Value("${rabbitmq.queue.profile.creation}")
    String profileCreation;

    @Value("${rabbitmq.queue.profile.creation-failed}")
    String profileCreationFailed;

    @Value("${rabbitmq.queue.profile.deletion}")
    String profileDeletion;

    @Value("${rabbitmq.queue.profile.deletion-failed}")
    String profileDeletionFailed;

    @Value("${rabbitmq.queue.profile.update}")
    String profileUpdate;

    @Value("${rabbitmq.queue.profile.update-failed}")
    String profileUpdateFailed;

    private final RabbitTemplate rabbitTemplate;


    public void profileCreation(ProfileCreationEvent event) {
        rabbitTemplate.convertAndSend(profileTopicExchange, profileCreation, event);
    }

    public void profileCreationFailed(ProfileCreationEventFailed event) {
        rabbitTemplate.convertAndSend(profileTopicExchange, profileCreationFailed, event);
    }

    public void profileDeleted(ProfileDeletionEvent event) {
        rabbitTemplate.convertAndSend(profileTopicExchange, profileDeletion, event);
    }

    public void profileDeletionFailed(ProfileDeletionFailedEvent event) {
        rabbitTemplate.convertAndSend(profileTopicExchange, profileDeletionFailed, event);
    }

    public void profileUpdate(ProfileUpdateEvent event) {
        rabbitTemplate.convertAndSend(profileTopicExchange, profileUpdate, event);
    }
}
