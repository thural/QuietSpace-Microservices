package com.jellybrains.quietspace_backend_ms.chatservice.service.impls;

import com.jellybrains.quietspace_backend_ms.chatservice.client.UserClient;
import com.jellybrains.quietspace_backend_ms.chatservice.entity.Chat;
import com.jellybrains.quietspace_backend_ms.chatservice.entity.Message;
import com.jellybrains.quietspace_backend_ms.chatservice.exception.UserNotFoundException;
import com.jellybrains.quietspace_backend_ms.chatservice.mapper.custom.MessageMapper;
import com.jellybrains.quietspace_backend_ms.chatservice.model.request.MessageRequest;
import com.jellybrains.quietspace_backend_ms.chatservice.model.response.MessageResponse;
import com.jellybrains.quietspace_backend_ms.chatservice.model.response.UserResponse;
import com.jellybrains.quietspace_backend_ms.chatservice.repository.ChatRepository;
import com.jellybrains.quietspace_backend_ms.chatservice.repository.MessageRepository;
import com.jellybrains.quietspace_backend_ms.chatservice.service.MessageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.jellybrains.quietspace_backend_ms.chatservice.utils.PagingProvider.buildPageRequest;


@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;
    private final UserClient userClient;

    @Override
    public MessageResponse addMessage(MessageRequest messageRequest) {

        if(!userClient.validateUserId(messageRequest.getSenderId()))
            throw new BadRequestException("requesting user mismatch with message body");

        Chat parentChat = chatRepository.findById(messageRequest.getChatId())
                .orElseThrow(() -> new BadRequestException("chat on request is not found"));

        Message newMessage = messageMapper.messageRequestToEntity(messageRequest);
        newMessage.setChat(parentChat);

        return messageMapper.messageEntityToDto(messageRepository.save(newMessage));
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message existingMessage = messageRepository.findById(messageId)
                .orElseThrow(EntityNotFoundException::new);

        if (existingMessage.getSenderId().equals(getLoggedUser().getId())) {
            messageRepository.deleteById(messageId);
        } else throw new BadRequestException("user mismatch with resource owner");
    }

    @Override
    public Page<MessageResponse> getMessagesByChatId(Integer pageNumber, Integer pageSize, UUID chatId) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,null);
        Page<Message> messagePage = messageRepository.findAllByChatId(chatId, pageRequest);
        return messagePage.map(messageMapper::messageEntityToDto);

    }

    @Override
    public Optional<MessageResponse> getLastMessageByChat(Chat chat) {
        return messageRepository.findFirstByChatOrderByCreateDateDesc(chat)
                .map(messageMapper::messageEntityToDto);
    }

    private UserResponse getLoggedUser(){
        return userClient.getLoggedUser().orElseThrow(UserNotFoundException::new);
    }

}
