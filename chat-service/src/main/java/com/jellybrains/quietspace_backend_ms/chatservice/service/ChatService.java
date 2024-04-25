package com.jellybrains.quietspace_backend_ms.chatservice.service;


import com.jellybrains.quietspace_backend_ms.chatservice.dto.request.ChatRequest;
import com.jellybrains.quietspace_backend_ms.chatservice.dto.response.ChatResponse;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    List<ChatResponse> getChatsByUserId(UUID userId);

    void deleteChatById(UUID chatId);

    void addMemberWithId(UUID memberId, UUID chatId);

    void removeMemberWithId(UUID memberId, UUID chatId);

    ChatResponse createChat(ChatRequest chatRequest);

    ChatResponse getChatById(UUID chatId);
}
