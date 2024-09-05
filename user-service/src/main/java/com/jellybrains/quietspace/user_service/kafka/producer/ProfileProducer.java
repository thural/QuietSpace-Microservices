package com.jellybrains.quietspace.user_service.kafka.producer;

import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileCreationEvent;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileCreationEventFailed;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileDeletionEvent;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileDeletionFailedEvent;
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

    @Value("${kafka.topics.profile}")
    private String profileTopic;

    private final KafkaTemplate<String, KafkaBaseEvent> kafkaTemplate;

    private <T> Message<T> prepareMessage(T payload) {
        return MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, profileTopic)
                .build();
    }

    public void profileDeletionFailed(ProfileDeletionFailedEvent event) {
        kafkaTemplate.send(prepareMessage(event));
    }

    public void profileCreation(ProfileCreationEvent event) {
        kafkaTemplate.send(prepareMessage(event));
    }

    public void profileCreationFailed(ProfileCreationEventFailed event) {
        kafkaTemplate.send(prepareMessage(event));
    }

    public void profileDeleted(ProfileDeletionEvent event) {
        kafkaTemplate.send(prepareMessage(event));
    }
}
