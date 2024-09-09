package com.jellybrains.quietspace.auth_service.kafka.producer;

import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import com.jellybrains.quietspace.common_service.message.kafka.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProducer {

    @Value("${kafka.topics.user.creation}")
    private String userCreationTopic;

    @Value("${kafka.topics.user.creation-failed}")
    private String userCreationFailedTopic;

    @Value("${kafka.topics.user.update-failed}")
    private String userUpdateFailedTopic;

    @Value("${kafka.topics.user.deletion}")
    private String userDeletionTopic;

    @Value("${kafka.topics.user.deletion-failed}")
    private String userDeletionFailedTopic;

    private final KafkaTemplate<String, KafkaBaseEvent> kafkaTemplate;

    private <T> Message<T> prepareMessage(T payload, String topic) {
        return MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
    }

    public void userCreation(UserCreationEvent event) {
        kafkaTemplate.send(prepareMessage(event, userCreationTopic));
    }

    public void userCreationFailed(UserCreationEventFailed event) {
        kafkaTemplate.send(prepareMessage(event, userCreationFailedTopic));
    }

    public void userDeletion(UserDeletionEvent event) {
        kafkaTemplate.send(prepareMessage(event, userDeletionTopic));
    }

    public void userDeletionFailed(UserDeletionFailedEvent event) {
        kafkaTemplate.send(prepareMessage(event, userDeletionFailedTopic));
    }

    public void userUpdateFailed(UserUpdateFailedEvent event) {
        kafkaTemplate.send(prepareMessage(event, userUpdateFailedTopic));
    }

}
