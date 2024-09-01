package com.jellybrains.quietspace.chat_service.service;

import com.jellybrains.quietspace.chat_service.entity.Chat;
import com.jellybrains.quietspace.common_service.model.request.MessageRequest;
import com.jellybrains.quietspace.common_service.model.response.MessageResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface MessageService {

    MessageResponse addMessage(MessageRequest messageRequest);

    Optional<MessageResponse> deleteMessage(String id);

    Page<MessageResponse> getMessagesByChatId(Integer pageNumber, Integer pageSize, String chatId);

    Optional<MessageResponse> getLastMessageByChat(Chat chat);

    Optional<MessageResponse> setMessageSeen(String messageId);
}
