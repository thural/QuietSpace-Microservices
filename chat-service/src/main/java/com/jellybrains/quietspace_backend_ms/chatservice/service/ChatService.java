package com.jellybrains.quietspace_backend_ms.chatservice.service;

import com.jellybrains.quietspace.common_service.model.request.ChatRequest;
import com.jellybrains.quietspace.common_service.model.response.ChatResponse;

import java.util.List;

public interface ChatService {

    List<ChatResponse> getChatsByUserId(String userId);

    void deleteChatById(String chatId);

    void addMemberWithId(String memberId, String chatId);

    List<String> removeMemberWithId(String memberId, String chatId);

    ChatResponse createChat(ChatRequest chatRequest);

    ChatResponse getChatById(String chatId);

}
