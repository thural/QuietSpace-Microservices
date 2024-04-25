package com.jellybrains.quietspace_backend_ms.chatservice.service;

import com.jellybrains.quietspace_backend_ms.chatservice.dto.request.MessageRequest;
import com.jellybrains.quietspace_backend_ms.chatservice.dto.response.MessageResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface MessageService {

    MessageResponse addMessage(MessageRequest messageRequest);

    void deleteMessage(UUID id);

    Page<MessageResponse> getMessagesByChatId(Integer pageNumber, Integer pageSiz, UUID chatId);
}
