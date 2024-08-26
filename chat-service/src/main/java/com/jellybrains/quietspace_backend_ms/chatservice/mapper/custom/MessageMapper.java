package com.jellybrains.quietspace_backend_ms.chatservice.mapper.custom;

import com.jellybrains.quietspace.common_service.model.request.MessageRequest;
import com.jellybrains.quietspace.common_service.model.response.MessageResponse;
import com.jellybrains.quietspace_backend_ms.chatservice.entity.Chat;
import com.jellybrains.quietspace_backend_ms.chatservice.entity.Message;
import com.jellybrains.quietspace_backend_ms.chatservice.repository.ChatRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final ChatRepository chatRepository;

    public Message toEntity(MessageRequest message) {
        return Message.builder()
                .text(message.getText())
                .chat(findChatById(message.getChatId()))
                .senderId(message.getSenderId())
                .recipientId(message.getRecipientId())
                .build();
    }

    public MessageResponse toResponse(Message message) {
        return MessageResponse
                .builder()
                .text(message.getText())
                .id(message.getId())
                .isSeen(message.getIsSeen())
                .chatId(message.getChat().getId())
                .senderId(message.getSenderId())
                .recipientId(message.getRecipientId())
                .senderName(message.getSenderId())
                .build();
    }

    private Chat findChatById(String chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(EntityNotFoundException::new);
    }
}
