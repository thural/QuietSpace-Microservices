package com.jellybrains.quietspace.chat_service.mapper.custom;

import com.jellybrains.quietspace.chat_service.entity.Chat;
import com.jellybrains.quietspace.chat_service.entity.Message;
import com.jellybrains.quietspace.chat_service.repository.ChatRepository;
import com.jellybrains.quietspace.common_service.model.request.MessageRequest;
import com.jellybrains.quietspace.common_service.model.response.MessageResponse;
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
