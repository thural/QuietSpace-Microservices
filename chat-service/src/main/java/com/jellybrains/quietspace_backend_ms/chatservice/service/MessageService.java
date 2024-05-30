package com.jellybrains.quietspace_backend_ms.chatservice.service;

import com.jellybrains.quietspace_backend_ms.chatservice.entity.Chat;
import com.jellybrains.quietspace_backend_ms.chatservice.model.request.MessageRequest;
import com.jellybrains.quietspace_backend_ms.chatservice.model.response.MessageResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface MessageService {

    MessageResponse addMessage(MessageRequest messageRequest);

    void deleteMessage(UUID id);

    Page<MessageResponse> getMessagesByChatId(Integer pageNumber, Integer pageSize, UUID chatId);

    Optional<MessageResponse> getLastMessageByChat(Chat chat);

}
