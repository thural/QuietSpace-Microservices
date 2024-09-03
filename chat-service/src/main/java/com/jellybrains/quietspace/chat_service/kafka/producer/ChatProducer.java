package com.jellybrains.quietspace.chat_service.kafka.producer;

import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import com.jellybrains.quietspace.common_service.message.kafka.chat.event.*;
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

    public void chatMessage(SendMessageEvent event) {
        kafkaTemplate.send(prepareMessage(event));
    }

    public void chatError(ChatErrorEvent event) {
        kafkaTemplate.send(prepareMessage(event));
    }

    public void deleteChatMessage(DeleteMessageEvent event) {
        kafkaTemplate.send(prepareMessage(event));
    }

    public void seenChatMessage(SeenMessageEvent event) {
        kafkaTemplate.send(prepareMessage(event));
    }

    public void leftChatEvent(LeftChatEvent event) {
        kafkaTemplate.send(prepareMessage(event));
    }

    public void joinedChatEvent(JoinedChatEvent event) {
        kafkaTemplate.send(prepareMessage(event));
    }

}
