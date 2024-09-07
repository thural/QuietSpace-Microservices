package com.jellybrains.quietspace.chat_service.kafka.consumer;

import com.jellybrains.quietspace.chat_service.entity.Message;
import com.jellybrains.quietspace.chat_service.kafka.producer.ChatProducer;
import com.jellybrains.quietspace.chat_service.repository.MessageRepository;
import com.jellybrains.quietspace.chat_service.service.ChatService;
import com.jellybrains.quietspace.chat_service.service.MessageService;
import com.jellybrains.quietspace.common_service.enums.EventType;
import com.jellybrains.quietspace.common_service.message.kafka.chat.event.*;
import com.jellybrains.quietspace.common_service.message.kafka.chat.request.*;
import com.jellybrains.quietspace.common_service.message.websocket.ChatEvent;
import com.jellybrains.quietspace.common_service.model.request.MessageRequest;
import com.jellybrains.quietspace.common_service.model.response.MessageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@RequiredArgsConstructor
public class ChatConsumer {

    private final ChatService chatService;
    private final ChatProducer chatProducer;
    private final MessageService messageService;
    private final MessageRepository messageRepository;


    @KafkaListener(topics = "#{'${kafka.topics.chat.event.send}'}")
    void sendMessageToUser(SendMessageRequest event) {
        MessageRequest message = event.getEventBody();
        log.info("saving received message: {} sent by: {}", message.getText(), message.getSenderId());
        try {
            MessageResponse savedMessage = messageService.addMessage(message);
            chatProducer.chatMessage(SendMessageEvent.builder().eventBody(savedMessage).build());
        } catch (Exception e) {
            var chatEvent = ChatEvent.builder()
                    .message(e.getMessage())
                    .chatId(message.getChatId())
                    .actorId(message.getSenderId())
                    .type(EventType.EXCEPTION)
                    .build();
            chatProducer.chatError(ChatErrorEvent.builder().eventBody(chatEvent).build());
        }
    }


    @KafkaListener(topics = "#{'${kafka.topics.chat.event.delete}'}")
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
            chatProducer.deleteChatMessage(DeleteMessageEvent.builder().eventBody(chatevent).build());
        } catch (Exception e) {

            chatevent.setMessage(e.getMessage());
            chatevent.setType(EventType.EXCEPTION);
            chatProducer.chatError(ChatErrorEvent.builder().eventBody(chatevent).build());
        }
    }


    @KafkaListener(topics = "#{'${kafka.topics.chat.event.seen}'}")
    void markMessageSeen(SeenMessageRequest event) {
        log.info("setting message with id {} as seen ...", event.getMessageId());
        MessageResponse message = messageService.setMessageSeen(event.getMessageId())
                .orElseThrow(EntityNotFoundException::new);
        var chatEvent = ChatEvent.builder()
                .chatId(message.getChatId())
                .messageId(message.getId())
                .type(EventType.SEEN_MESSAGE)
                .build();
        chatProducer.seenChatMessage(SeenMessageEvent.builder().eventBody(chatEvent).build());
    }


    @KafkaListener(topics = "#{'${kafka.topics.chat.event.leave}'}")
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
            chatProducer.leftChatEvent(LeftChatEvent.builder().eventBody(chatEvent).build());
        } catch (Exception e) {
            chatEvent.setMessage(e.getMessage());
            chatEvent.setType(EventType.EXCEPTION);
            chatProducer.chatError(ChatErrorEvent.builder().eventBody(chatEvent).build());
        }
    }


    @KafkaListener(topics = "#{'${kafka.topics.chat.event.join}'}")
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
            chatProducer.joinedChatEvent(JoinedChatEvent.builder().eventBody(chatEvent).build());
        } catch (Exception e) {
            chatEvent.setMessage(e.getMessage());
            chatEvent.setType(EventType.EXCEPTION);
            chatProducer.chatError(ChatErrorEvent.builder().eventBody(chatEvent).build());
        }
    }


}
