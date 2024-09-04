package com.jellybrains.quietspace.auth_service.kafka.producer;

import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileCreationEvent;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileDeletionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileProducer {

    private final NewTopic profileTopic;
    private final KafkaTemplate<String, KafkaBaseEvent> kafkaTemplate;

    private <T> Message<T> prepareMessage(T payload) {
        return MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, profileTopic)
                .build();
    }

    public void profileCreation(ProfileCreationEvent event) {
        kafkaTemplate.send(prepareMessage(event));
    }

    public void profileDeletion(ProfileDeletionEvent event) {
        kafkaTemplate.send(prepareMessage(event));
    }

}
