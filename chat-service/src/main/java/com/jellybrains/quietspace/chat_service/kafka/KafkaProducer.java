package com.jellybrains.quietspace.chat_service.kafka;

import com.jellybrains.quietspace.common_service.message.kafka.chat.event.ReceiveMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, ReceiveMessageEvent> kafkaTemplate;

    public void sendNewChatMessage(ReceiveMessageEvent event) {
        Message<ReceiveMessageEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "chat-topic")
                .build();
        kafkaTemplate.send(message);
    }

}
