package com.jellybrains.quietspace.chat_service.kafka.consumer;

import com.jellybrains.quietspace.chat_service.kafka.producer.ChatProducer;
import com.jellybrains.quietspace.chat_service.repository.MessageRepository;
import com.jellybrains.quietspace.chat_service.service.ChatService;
import com.jellybrains.quietspace.chat_service.service.MessageService;
import com.jellybrains.quietspace.common_service.enums.EventType;
import com.jellybrains.quietspace.common_service.exception.CustomErrorException;
import com.jellybrains.quietspace.common_service.exception.CustomNotFoundException;
import com.jellybrains.quietspace.common_service.message.kafka.chat.event.*;
import com.jellybrains.quietspace.common_service.message.kafka.chat.request.*;
import com.jellybrains.quietspace.common_service.message.websocket.ChatEvent;
import com.jellybrains.quietspace.common_service.model.request.MessageRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import reactor.core.publisher.Mono;

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
            messageService.addMessage(message)
                    .switchIfEmpty(Mono.error(CustomErrorException::new))
                    .subscribe(messageResponse -> chatProducer.chatMessage(SendMessageEvent.builder()
                                    .eventBody(messageResponse).build()),
                            error -> log.info("failed to produce sendMessageEvent: {}", error.getMessage()),
                            () -> log.info("successfully produced sendMessageEvent")
                    );
        } catch (Exception e) {
            log.info("failed to persist data on sendMessageRequest: {}", e.getMessage());
            var chatEvent = ChatEvent.builder()
                    .message(e.getMessage())
                    .chatId(message.getChatId())
                    .actorId(message.getSenderId())
                    .type(EventType.EXCEPTION).build();
            chatProducer.chatError(ChatErrorEvent.builder().eventBody(chatEvent).build());
        }
    }


    @KafkaListener(topics = "#{'${kafka.topics.chat.event.delete}'}")
    void deleteMessageById(DeleteMessageRequest event) {
        log.info("deleting message with id {} ...", event.getMessageId());
        Mono<ChatEvent> mono = messageRepository.findById(event.getMessageId())
                .switchIfEmpty(Mono.error(EntityNotFoundException::new))
                .map(message -> ChatEvent.builder()
                        .chatId(message.getChat().getId())
                        .actorId(message.getSenderId())
                        .messageId(message.getId())
                        .type(EventType.DELETE_MESSAGE).build()
                );
        try {
            mono.subscribe(chatEvent -> messageService.deleteMessage(event.getMessageId())
                            .switchIfEmpty(Mono.error(EntityNotFoundException::new))
                            .doOnNext(message -> {
                                assert chatEvent != null;
                                chatEvent.setChatId(message.getChatId());
                                chatProducer.deleteChatMessage(DeleteMessageEvent.builder()
                                        .eventBody(chatEvent).build()
                                );
                            }),
                    error -> log.info("failed to delete message on deleteMessageRequest: {}", error.getMessage()),
                    () -> log.info("message deletion success on deleteMessageRequest")
            );
        } catch (Exception e) {
            log.info("failed to persist data on deleteMessageRequest: {}", e.getMessage());
            mono.subscribe(chatEvent -> {
                        chatEvent.setMessage(chatEvent.getMessage());
                        chatEvent.setType(EventType.EXCEPTION);
                        chatProducer.chatError(ChatErrorEvent.builder().eventBody(chatEvent).build());
                    },
                    error -> log.info("failed to produce chatErrorEvent: {}", error.getMessage()),
                    () -> log.info("produced chatErrorEvent on deleteMessageRequest")
            );
        }
    }


    @KafkaListener(topics = "#{'${kafka.topics.chat.event.seen}'}")
    void markMessageSeen(SeenMessageRequest event) {
        log.info("setting message with id {} as seen ...", event.getMessageId());
        messageService.setMessageSeen(event.getMessageId())
                .switchIfEmpty(Mono.error(EntityNotFoundException::new))
                .map(message -> ChatEvent.builder()
                        .chatId(message.getChatId())
                        .messageId(message.getId())
                        .type(EventType.SEEN_MESSAGE).build()
                )
                .subscribe(chatEvent -> chatProducer.seenChatMessage(
                        SeenMessageEvent.builder()
                                .eventBody(chatEvent).build())
                );
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
            chatService.removeMemberWithId(eventBody.getActorId(), eventBody.getChatId())
                    .switchIfEmpty(Mono.error(CustomErrorException::new))
                    .collectList().doOnNext(chatEvent::setUserIds)
                    .subscribe(list -> chatProducer.leftChatEvent(LeftChatEvent.builder()
                                    .eventBody(chatEvent).build()),
                            error -> log.info("failed to produce leftChatEvent"),
                            () -> log.info("produced leftChatEvent on leaveChatRequest")
                    );
        } catch (Exception e) {
            log.info("failed to persist data on leaveChatRequest: {}", e.getMessage());
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
            chatService.addMemberWithId(eventBody.getRecipientId(), eventBody.getChatId())
                    .switchIfEmpty(Mono.error(CustomNotFoundException::new))
                    .collectList().doOnNext(chatEvent::setUserIds)
                    .doOnNext(userIds -> chatEvent.setMessage(String.format(
                            "user %s has been added to chat %s ...",
                            eventBody.getRecipientId(),
                            eventBody.getChatId()
                    )))
                    .subscribe(userIds -> chatProducer.joinedChatEvent(JoinedChatEvent.builder().eventBody(chatEvent).build()),
                            error -> log.info("failed to produce joinedChatEvent: {}", error.getMessage()),
                            () -> log.info("produced joinedChatEvent on joinChatRequest")
                    );
        } catch (Exception e) {
            log.info("failed to persist data on joinChatRequest: {}", e.getMessage());
            chatEvent.setMessage(e.getMessage());
            chatEvent.setType(EventType.EXCEPTION);
            chatProducer.chatError(ChatErrorEvent.builder().eventBody(chatEvent).build());
        }
    }


}
