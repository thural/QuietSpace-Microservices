package com.jellybrains.quietspace.user_service.kafka.producer;

import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import com.jellybrains.quietspace.common_service.message.kafka.profile.*;
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
public class ProfileProducer {

    @Value("${kafka.topics.profile.creation}")
    private String profileCreationTopic;

    @Value("${kafka.topics.profile.creation-failed}")
    private String profileCreationFailedTopic;

    @Value("${kafka.topics.profile.update}")
    private String profileUpdateTopic;

    @Value("${kafka.topics.profile.deletion}")
    private String profileDeletionTopic;

    @Value("${kafka.topics.profile.deletion-failed}")
    private String profileDeletionFailedTopic;


    private final KafkaTemplate<String, KafkaBaseEvent> kafkaTemplate;

    private <T> Message<T> prepareMessage(T payload, String topic) {
        return MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
    }


    public void profileCreation(ProfileCreationEvent event) {
        kafkaTemplate.send(prepareMessage(event, profileCreationTopic));
    }

    public void profileCreationFailed(ProfileCreationEventFailed event) {
        kafkaTemplate.send(prepareMessage(event, profileCreationFailedTopic));
    }

    public void profileDeleted(ProfileDeletionEvent event) {
        kafkaTemplate.send(prepareMessage(event, profileDeletionTopic));
    }

    public void profileDeletionFailed(ProfileDeletionFailedEvent event) {
        kafkaTemplate.send(prepareMessage(event, profileDeletionFailedTopic));
    }

    public void profileUpdate(ProfileUpdateEvent event) {
        kafkaTemplate.send(prepareMessage(event, profileUpdateTopic));
    }
}
