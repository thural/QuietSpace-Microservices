package com.jellybrains.quietspace.chat_service.service;

import com.jellybrains.quietspace.common_service.model.request.ChatRequest;
import com.jellybrains.quietspace.common_service.model.response.ChatResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ChatService {

    Flux<ChatResponse> getChatsByUserId(String userId);

    Mono<Void> deleteChatById(String chatId);

    Flux<String> addMemberWithId(String memberId, String chatId);

    Flux<String> removeMemberWithId(String memberId, String chatId);

    Mono<ChatResponse> createChat(ChatRequest chatRequest);

    Mono<ChatResponse> getChatById(String chatId);

}
