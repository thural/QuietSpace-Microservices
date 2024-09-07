package com.jellybrains.quietspace.chat_service.kafka.producer;

import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import com.jellybrains.quietspace.common_service.message.kafka.chat.event.*;
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
public class ChatProducer {

    @Value("${kafka.topics.chat.event.send}")
    private String chatEventSendTopic;

    @Value("${kafka.topics.chat.event.delete}")
    private String chatEventDeleteTopic;

    @Value("${kafka.topics.chat.event.seen}")
    private String chatEventSeenTopic;

    @Value("${kafka.topics.chat.event.leave}")
    private String chatEventLeaveTopic;

    @Value("${kafka.topics.chat.event.join}")
    private String chatEventJoinTopic;

    @Value("${kafka.topics.chat.event.error}")
    private String chatEventErrorTopic;

    private final KafkaTemplate<String, KafkaBaseEvent> kafkaTemplate;

    private <T> Message<T> prepareMessage(T payload, String topic) {
        return MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
    }

    public void chatMessage(SendMessageEvent event) {
        kafkaTemplate.send(prepareMessage(event, chatEventSendTopic));
    }

    public void chatError(ChatErrorEvent event) {
        kafkaTemplate.send(prepareMessage(event, chatEventErrorTopic));
    }

    public void deleteChatMessage(DeleteMessageEvent event) {
        kafkaTemplate.send(prepareMessage(event, chatEventDeleteTopic));
    }

    public void seenChatMessage(SeenMessageEvent event) {
        kafkaTemplate.send(prepareMessage(event, chatEventSeenTopic));
    }

    public void leftChatEvent(LeftChatEvent event) {
        kafkaTemplate.send(prepareMessage(event, chatEventLeaveTopic));
    }

    public void joinedChatEvent(JoinedChatEvent event) {
        kafkaTemplate.send(prepareMessage(event, chatEventJoinTopic));
    }

}
