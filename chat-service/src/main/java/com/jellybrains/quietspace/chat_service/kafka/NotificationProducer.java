package com.jellybrains.quietspace.chat_service.kafka;

import com.jellybrains.quietspace.chat_service.event.NewMessageEvent;
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
public class NotificationProducer {

    private final KafkaTemplate<String, NewMessageEvent> kafkaTemplate;

    public void sendNewMessageNotification(NewMessageEvent event) {
        log.info("sending message notification");
        Message<NewMessageEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "notification-topic")
                .build();
        kafkaTemplate.send(message);
    }

}
