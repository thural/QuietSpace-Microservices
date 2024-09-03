package com.jellybrains.quietspace.common_service.message.kafka.chat.producer;

import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import com.jellybrains.quietspace.common_service.message.kafka.chat.request.*;
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
public class ChatProducer {

    private final NewTopic chatTopic;
    private final KafkaTemplate<String, KafkaBaseEvent> kafkaTemplate;

    private <T> Message<T> prepareMessage(T payload) {
        return MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, chatTopic)
                .build();
    }

    public void chatMessageRequest(SendMessageRequest event) {
        kafkaTemplate.send(prepareMessage(event));
    }

    public void deleteChatMessageRequest(DeleteMessageRequest event) {
        kafkaTemplate.send(prepareMessage(event));
    }

    public void seenChatRequest(SeenMessageRequest event) {
        kafkaTemplate.send(prepareMessage(event));
    }

    public void leaveChatRequest(LeaveChatRequest event) {
        kafkaTemplate.send(prepareMessage(event));
    }

    public void joinChatRequest(JoinChatRequest event) {
        kafkaTemplate.send(prepareMessage(event));
    }

}
