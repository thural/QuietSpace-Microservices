package com.jellybrains.quietspace.common_service.kafka.consumer;

import com.jellybrains.quietspace.common_service.message.kafka.chat.event.*;
import com.jellybrains.quietspace.common_service.message.websocket.ChatEvent;
import com.jellybrains.quietspace.common_service.model.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.jellybrains.quietspace.common_service.constant.ChatPathValues.CHAT_EVENT_PATH;
import static com.jellybrains.quietspace.common_service.constant.ChatPathValues.SOCKET_CHAT_PATH;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatConsumer {

    private final SimpMessagingTemplate template;

    @KafkaListener(topics = "#{'${kafka.topics.chat.event.send}'}")
    public void processSendMessage(SendMessageEvent event) {
        MessageResponse messageBody = event.getEventBody();
        template.convertAndSendToUser(messageBody.getRecipientId(), SOCKET_CHAT_PATH, messageBody);
        template.convertAndSendToUser(messageBody.getSenderId(), SOCKET_CHAT_PATH, messageBody);
        log.info("message sent to userId: {}", messageBody.getRecipientId());
    }

    @KafkaListener(topics = "#{'${kafka.topics.chat.event.delete}'}")
    public void deleteMessageById(DeleteMessageEvent event) {
        ChatEvent chatEvent = event.getEventBody();
        template.convertAndSendToUser(chatEvent.getRecipientId(), CHAT_EVENT_PATH, chatEvent);
        template.convertAndSendToUser(chatEvent.getActorId(), CHAT_EVENT_PATH, chatEvent);
        log.info("deleted message with id {} ...", chatEvent.getMessageId());
    }

    @KafkaListener(topics = "#{'${kafka.topics.chat.event.seen}'}")
    public void processSeenMessage(SeenMessageEvent event) {
        ChatEvent chatEvent = event.getEventBody();
        template.convertAndSendToUser(chatEvent.getActorId(), CHAT_EVENT_PATH, chatEvent);
        template.convertAndSendToUser(chatEvent.getRecipientId(), CHAT_EVENT_PATH, chatEvent);
        log.info("set message with id {} as seen ...", chatEvent.getMessageId());
    }

    @KafkaListener(topics = "#{'${kafka.topics.chat.event.leave}'}")
    void processLeaveChat(LeftChatEvent event) {
        ChatEvent chatEvent = event.getEventBody();
        chatEvent.getUserIds().forEach(userId -> {
            template.convertAndSendToUser(userId, SOCKET_CHAT_PATH, chatEvent);
        });
        log.info("user {} has leave chat {}", chatEvent.getActorId(), chatEvent.getChatId());
    }

    @KafkaListener(topics = "#{'${kafka.topics.chat.event.join}'}")
    void processJoinChat(JoinedChatEvent event) {
        ChatEvent chatEvent = event.getEventBody();
        chatEvent.getUserIds().forEach(userId -> {
            template.convertAndSendToUser(userId, SOCKET_CHAT_PATH, chatEvent);
        });
        log.info("user {} has joined chat {}", chatEvent.getActorId(), chatEvent.getChatId());
    }

    @KafkaListener(topics = "#{'${kafka.topics.chat.event.error}'}")
    void processChatError(ChatErrorEvent event) {
        ChatEvent chatEvent = event.getEventBody();
        log.info("chat exception occurred: {}", chatEvent.getMessage());
        if (Objects.nonNull(chatEvent.getUserIds())) {
            chatEvent.getUserIds().forEach(userId -> {
                template.convertAndSendToUser(userId, CHAT_EVENT_PATH, chatEvent);
            });
        } else template.convertAndSendToUser(chatEvent.getActorId(), CHAT_EVENT_PATH, chatEvent);
    }

}