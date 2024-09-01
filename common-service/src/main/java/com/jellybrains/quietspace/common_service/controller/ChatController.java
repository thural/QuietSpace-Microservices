package com.jellybrains.quietspace.common_service.controller;

import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import com.jellybrains.quietspace.common_service.message.kafka.chat.event.SendMessageEvent;
import com.jellybrains.quietspace.common_service.message.kafka.chat.request.DeleteMessageRequest;
import com.jellybrains.quietspace.common_service.message.kafka.chat.request.JoinChatRequest;
import com.jellybrains.quietspace.common_service.message.kafka.chat.request.LeaveChatRequest;
import com.jellybrains.quietspace.common_service.message.kafka.chat.request.SeenMessageRequest;
import com.jellybrains.quietspace.common_service.message.websocket.ChatEvent;
import com.jellybrains.quietspace.common_service.model.request.MessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import static com.jellybrains.quietspace.common_service.constant.ChatPathValues.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    @Value("${kafka.topics.chat}")
    private String chatTopic;

    private final KafkaTemplate<String, KafkaBaseEvent> kafkaTemplate;


    @MessageMapping(PUBLIC_CHAT_PATH)
    @SendTo(PUBLIC_CHAT_PATH)
    MessageRequest sendMessageToAll(final MessageRequest message) {
        log.info("received message at {} topic: {}", PUBLIC_CHAT_PATH, message.getText());
        return message;
    }

    @MessageMapping(SOCKET_CHAT_PATH)
    void sendMessageToUser(@Payload MessageRequest message) {
        log.info("received topic: {}, sent by: {}", message.getText(), message.getSenderId());
        kafkaTemplate.send(chatTopic, SendMessageEvent.builder().eventBody(message).build());
    }

    @MessageMapping(DELETE_MESSAGE_PATH)
    void deleteMessageById(@DestinationVariable String messageId) {
        log.info("deleting message with id {} ...", messageId);
        kafkaTemplate.send(chatTopic, DeleteMessageRequest.builder().messageId(messageId).build());
    }

    @MessageMapping(SEEN_MESSAGE_PATH)
    void processSeenRequest(@DestinationVariable String messageId) {
        log.info("setting message with id {} as seen ...", messageId);
        kafkaTemplate.send(chatTopic, SeenMessageRequest.builder().messageId(messageId).build());
    }

    @MessageMapping(LEAVE_CHAT_PATH)
    void processLeftChat(@Payload ChatEvent event) {
        log.info("user {} is leaving chat {} ...", event.getActorId(), event.getChatId());
        kafkaTemplate.send(chatTopic, LeaveChatRequest.builder().eventBody(event).build());
    }

    @MessageMapping(JOIN_CHAT_PATH)
    void processJoinChat(@Payload ChatEvent event) {
        log.info("user {} is being added to chat {} ...", event.getRecipientId(), event.getChatId());
        kafkaTemplate.send(chatTopic, JoinChatRequest.builder().eventBody(event).build());
    }

}
