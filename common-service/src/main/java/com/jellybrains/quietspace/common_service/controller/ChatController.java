package com.jellybrains.quietspace.common_service.controller;

import com.jellybrains.quietspace.common_service.kafka.producer.ChatProducer;
import com.jellybrains.quietspace.common_service.message.kafka.chat.request.*;
import com.jellybrains.quietspace.common_service.message.websocket.ChatEvent;
import com.jellybrains.quietspace.common_service.model.request.MessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final ChatProducer chatProducer;


    @MessageMapping(PUBLIC_CHAT_PATH)
    @SendTo(PUBLIC_CHAT_PATH)
    MessageRequest sendMessageToAll(final MessageRequest message) {
        log.info("received message at {} topic: {}", PUBLIC_CHAT_PATH, message.getText());
        return message;
    }

    @MessageMapping(SOCKET_CHAT_PATH)
    void sendMessageToUser(@Payload MessageRequest message) {
        log.info("received topic: {}, sent by: {}", message.getText(), message.getSenderId());
        chatProducer.chatMessageRequest(SendMessageRequest.builder().eventBody(message).build());
    }

    @MessageMapping(DELETE_MESSAGE_PATH)
    void deleteMessageById(@DestinationVariable String messageId) {
        log.info("deleting message with id {} ...", messageId);
        chatProducer.deleteChatMessageRequest(DeleteMessageRequest.builder().messageId(messageId).build());
    }

    @MessageMapping(SEEN_MESSAGE_PATH)
    void processSeenRequest(@DestinationVariable String messageId) {
        log.info("setting message with id {} as seen ...", messageId);
        chatProducer.seenChatRequest(SeenMessageRequest.builder().messageId(messageId).build());
    }

    @MessageMapping(LEAVE_CHAT_PATH)
    void processLeftChat(@Payload ChatEvent event) {
        log.info("user {} is leaving chat {} ...", event.getActorId(), event.getChatId());
        chatProducer.leaveChatRequest(LeaveChatRequest.builder().eventBody(event).build());
    }

    @MessageMapping(JOIN_CHAT_PATH)
    void processJoinChat(@Payload ChatEvent event) {
        log.info("user {} is being added to chat {} ...", event.getRecipientId(), event.getChatId());
        chatProducer.joinChatRequest(JoinChatRequest.builder().eventBody(event).build());
    }

}
