package com.jellybrains.quietspace.common_service.kafka.producer;

import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import com.jellybrains.quietspace.common_service.message.kafka.chat.request.*;
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

    @Value("${kafka.topics.chat.request.send}")
    private String chatRequestSendTopic;

    @Value("${kafka.topics.chat.request.delete}")
    private String chatRequestDeleteTopic;

    @Value("${kafka.topics.chat.request.seen}")
    private String chatRequestSeenTopic;

    @Value("${kafka.topics.chat.request.leave}")
    private String chatRequestLeaveTopic;

    @Value("${kafka.topics.chat.request.join}")
    private String chatRequestJoinTopic;


    private final KafkaTemplate<String, KafkaBaseEvent> kafkaTemplate;

    private <T> Message<T> prepareMessage(T payload, String topic) {
        return MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
    }


    public void sendChatMessageRequest(SendMessageRequest event) {
        kafkaTemplate.send(prepareMessage(event, chatRequestSendTopic));
    }

    public void deleteChatMessageRequest(DeleteMessageRequest event) {
        kafkaTemplate.send(prepareMessage(event, chatRequestDeleteTopic));
    }

    public void seenChatRequest(SeenMessageRequest event) {
        kafkaTemplate.send(prepareMessage(event, chatRequestSeenTopic));
    }

    public void leaveChatRequest(LeaveChatRequest event) {
        kafkaTemplate.send(prepareMessage(event, chatRequestLeaveTopic));
    }

    public void joinChatRequest(JoinChatRequest event) {
        kafkaTemplate.send(prepareMessage(event, chatRequestJoinTopic));
    }

}
