package com.jellybrains.quietspace.chat_service.service;

import com.jellybrains.quietspace.chat_service.entity.Chat;
import com.jellybrains.quietspace.common_service.model.request.MessageRequest;
import com.jellybrains.quietspace.common_service.model.response.MessageResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MessageService {

    Mono<MessageResponse> addMessage(MessageRequest messageRequest);

    Mono<MessageResponse> deleteMessage(String id);

    Flux<MessageResponse> getMessagesByChatId(Integer pageNumber, Integer pageSize, String chatId);

    Mono<MessageResponse> getLastMessageByChat(Chat chat);

    Mono<MessageResponse> setMessageSeen(String messageId);
}
