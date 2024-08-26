package com.jellybrains.quietspace_backend_ms.chatservice.service;

import com.jellybrains.quietspace.common_service.model.request.MessageRequest;
import com.jellybrains.quietspace.common_service.model.response.MessageResponse;
import com.jellybrains.quietspace_backend_ms.chatservice.entity.Chat;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface MessageService {

    MessageResponse addMessage(MessageRequest messageRequest);

    Optional<MessageResponse> deleteMessage(String id);

    Page<MessageResponse> getMessagesByChatId(Integer pageNumber, Integer pageSize, String chatId);

    Optional<MessageResponse> getLastMessageByChat(Chat chat);

    Optional<MessageResponse> setMessageSeen(String messageId);
}
