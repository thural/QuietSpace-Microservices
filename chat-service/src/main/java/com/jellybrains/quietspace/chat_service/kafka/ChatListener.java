package com.jellybrains.quietspace.chat_service.kafka;

import com.jellybrains.quietspace.chat_service.entity.Message;
import com.jellybrains.quietspace.chat_service.repository.MessageRepository;
import com.jellybrains.quietspace.chat_service.service.ChatService;
import com.jellybrains.quietspace.chat_service.service.MessageService;
import com.jellybrains.quietspace.common_service.enums.EventType;
import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import com.jellybrains.quietspace.common_service.message.kafka.chat.event.*;
import com.jellybrains.quietspace.common_service.message.kafka.chat.request.*;
import com.jellybrains.quietspace.common_service.message.websocket.ChatEvent;
import com.jellybrains.quietspace.common_service.model.request.MessageRequest;
import com.jellybrains.quietspace.common_service.model.response.MessageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@RequiredArgsConstructor
public class ChatListener {

    @Value("${kafka.topics.chat}")
    private String chatTopic;

    private final ChatService chatService;
    private final MessageService messageService;
    private final MessageRepository messageRepository;
    private final KafkaTemplate<String, KafkaBaseEvent> kafkaTemplate;


    @KafkaListener(topics = "#{'${kafka.topics.chat}'}")
    void sendMessageToUser(SendMessageRequest event) {
        MessageRequest message = event.getEventBody();
        log.info("saving received message at {} topic: {}, sent by: {}", chatTopic, message.getText(), message.getSenderId());

        try {
            MessageResponse savedMessage = messageService.addMessage(message);
            kafkaTemplate.send(chatTopic, ReceiveMessageEvent.builder().eventBody(savedMessage).build());
        } catch (Exception e) {
            var chatEvent = ChatEvent.builder()
                    .message(e.getMessage())
                    .chatId(message.getChatId())
                    .actorId(message.getSenderId())
                    .type(EventType.EXCEPTION)
                    .build();

            kafkaTemplate.send(chatTopic, ChatErrorEvent.builder().eventBody(chatEvent).build());
        }
    }


    @KafkaListener(topics = "#{'${kafka.topics.chat}'}")
    void deleteMessageById(DeleteMessageRequest event) {
        log.info("deleting message with id {} ...", event.getMessageId());
        Message foundMessage = messageRepository.findById(event.getMessageId())
                .orElseThrow(EntityNotFoundException::new);

        var chatevent = ChatEvent.builder()
                .chatId(foundMessage.getChat().getId())
                .actorId(foundMessage.getSenderId())
                .messageId(foundMessage.getId())
                .type(EventType.DELETE_MESSAGE)
                .build();
        try {
            MessageResponse message = messageService.deleteMessage(event.getMessageId())
                    .orElseThrow(RuntimeException::new);
            chatevent.setChatId(message.getChatId());
            kafkaTemplate.send(chatTopic, DeleteMessageEvent.builder().eventBody(chatevent).build());
        } catch (Exception e) {

            chatevent.setMessage(e.getMessage());
            chatevent.setType(EventType.EXCEPTION);
            kafkaTemplate.send(chatTopic, ChatErrorEvent.builder().eventBody(chatevent).build());
        }
    }


    @KafkaListener(topics = "#{'${kafka.topics.chat}'}")
    void markMessageSeen(SeenMessageRequest event) {
        log.info("setting message with id {} as seen ...", event.getMessageId());

        MessageResponse message = messageService.setMessageSeen(event.getMessageId())
                .orElseThrow(EntityNotFoundException::new);

        var chatEvent = ChatEvent.builder()
                .chatId(message.getChatId())
                .messageId(message.getId())
                .type(EventType.SEEN_MESSAGE)
                .build();

        kafkaTemplate.send(chatTopic, SeenMessageEvent.builder().eventBody(chatEvent).build());
    }


    @KafkaListener(topics = "#{'${kafka.topics.chat}'}")
    void processLeftChat(LeaveChatRequest event) {
        ChatEvent eventBody = event.getEventBody();
        log.info("user {} is leaving chat {} ...", eventBody.getActorId(), eventBody.getChatId());
        var chatEvent = ChatEvent.builder()
                .message("user has left the chat")
                .chatId(eventBody.getChatId())
                .actorId(eventBody.getActorId())
                .type(EventType.LEFT_CHAT)
                .build();
        try {
            var userList = chatService.removeMemberWithId(eventBody.getActorId(), eventBody.getChatId());
            chatEvent.setUserIds(userList);
            kafkaTemplate.send(chatTopic, LeftChatEvent.builder().eventBody(chatEvent).build());
        } catch (Exception e) {
            chatEvent.setMessage(e.getMessage());
            chatEvent.setType(EventType.EXCEPTION);
            kafkaTemplate.send(chatTopic, ChatErrorEvent.builder().eventBody(chatEvent).build());
        }
    }


    @KafkaListener(topics = "#{'${kafka.topics.chat}'}")
    void processJoinChat(JoinChatRequest event) {
        ChatEvent eventBody = event.getEventBody();
        log.info("user {} is being added to chat {} ...", eventBody.getRecipientId(), eventBody.getChatId());
        var chatEvent = ChatEvent.builder()
                .chatId(eventBody.getChatId())
                .actorId(eventBody.getActorId())
                .type(EventType.JOINED_CHAT)
                .build();
        try {
            var userList = chatService.addMemberWithId(eventBody.getRecipientId(), eventBody.getChatId());
            chatEvent.setUserIds(userList);
            chatEvent.setMessage(String.format(
                    "user %s has been added to chat %s ...",
                    eventBody.getRecipientId(),
                    eventBody.getChatId()
            ));

            kafkaTemplate.send(chatTopic, JoinedChatEvent.builder().eventBody(chatEvent).build());

        } catch (Exception e) {
            chatEvent.setMessage(e.getMessage());
            chatEvent.setType(EventType.EXCEPTION);
            kafkaTemplate.send(chatTopic, ChatErrorEvent.builder().eventBody(chatEvent).build());
        }
    }


}
