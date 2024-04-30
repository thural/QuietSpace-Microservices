package com.jellybrains.quietspace_backend_ms.chatservice.service.impls;

import com.jellybrains.quietspace_backend_ms.chatservice.dto.request.MessageRequest;
import com.jellybrains.quietspace_backend_ms.chatservice.dto.response.MessageResponse;
import com.jellybrains.quietspace_backend_ms.chatservice.event.MessageSentEvent;
import com.jellybrains.quietspace_backend_ms.chatservice.mapper.MessageMapper;
import com.jellybrains.quietspace_backend_ms.chatservice.model.Chat;
import com.jellybrains.quietspace_backend_ms.chatservice.model.Message;
import com.jellybrains.quietspace_backend_ms.chatservice.repository.ChatRepository;
import com.jellybrains.quietspace_backend_ms.chatservice.repository.MessageRepository;
import com.jellybrains.quietspace_backend_ms.chatservice.service.MessageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.jellybrains.quietspace_backend_ms.chatservice.utils.PagingProvider.buildCustomPageRequest;


@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final KafkaTemplate<String, MessageSentEvent> kafkaTemplate;
    // TODO: implement webflux to get user data
    private final MessageMapper messageMapper;

    UUID getLoggedUserId(){
        // TODO: retrieve current user
        return null;
    }

    @Override
    public MessageResponse addMessage(MessageRequest messageRequest) {

        UUID loggedUserId = getLoggedUserId();

        Chat parentChat = chatRepository.findById(messageRequest.getChatId())
                .orElseThrow(EntityNotFoundException::new);

        Message newMessage = messageMapper.messageRequestToEntity(messageRequest);
        newMessage.setSenderId(loggedUserId);
        newMessage.setChat(parentChat);

        MessageSentEvent messageSentEvent = MessageSentEvent.builder()
                .message(newMessage.getText())
                .chatId(newMessage.getChat().getId().toString())
                .senderId(loggedUserId.toString())
                .build();

        kafkaTemplate.send("notificationTopic", messageSentEvent);

        return messageMapper.messageEntityToResponse(messageRepository.save(newMessage));
    }

    @Override
    public void deleteMessage(UUID messageId) {
        UUID loggedUserId = getLoggedUserId();

        Message existingMessage = messageRepository.findById(messageId)
                .orElseThrow(EntityNotFoundException::new);

        if (existingMessage.getSenderId().equals(loggedUserId)) {
            messageRepository.deleteById(messageId);
        } // TODO: else throw _message does not belong to current user_ exceptions
    }

    @Override
    public Page<MessageResponse> getMessagesByChatId(Integer pageNumber, Integer pageSize, UUID chatId) {
        PageRequest pageRequest = buildCustomPageRequest(pageNumber, pageSize);
        Page<Message> messagePage = messageRepository.findAllByChatId(chatId, pageRequest);
        return messagePage.map(messageMapper::messageEntityToResponse);
    }

}
