package com.jellybrains.quietspace.chat_service.service.impls;

import com.jellybrains.quietspace.chat_service.entity.Chat;
import com.jellybrains.quietspace.chat_service.entity.Message;
import com.jellybrains.quietspace.chat_service.mapper.custom.MessageMapper;
import com.jellybrains.quietspace.chat_service.repository.ChatRepository;
import com.jellybrains.quietspace.chat_service.repository.MessageRepository;
import com.jellybrains.quietspace.chat_service.service.MessageService;
import com.jellybrains.quietspace.common_service.exception.CustomErrorException;
import com.jellybrains.quietspace.common_service.model.request.MessageRequest;
import com.jellybrains.quietspace.common_service.model.response.MessageResponse;
import com.jellybrains.quietspace.common_service.webclient.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.jellybrains.quietspace.common_service.utils.PagingProvider.buildPageRequest;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;
    private final UserService userService;

    @Override
    public MessageResponse addMessage(MessageRequest messageRequest) {
        log.info("message at addMessage method: {}", messageRequest.getText());
        String loggedUser = userService.getAuthorizedUserId();

        Chat parentChat = chatRepository.findById(messageRequest.getChatId())
                .orElseThrow(EntityNotFoundException::new);

        Message newMessage = messageMapper.toEntity(messageRequest);
        newMessage.setSenderId(loggedUser);
        newMessage.setChat(parentChat);

        return messageMapper.toResponse(messageRepository.save(newMessage));
    }

    @Override
    public Optional<MessageResponse> deleteMessage(String messageId) {
        Message existingMessage = findMessageOrElseThrow(messageId);
        checkResourceAccess(existingMessage.getSenderId());

        messageRepository.deleteById(messageId);
        return Optional.of(messageMapper.toResponse(existingMessage));
    }

    private Message findMessageOrElseThrow(String messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(EntityNotFoundException::new);
    }

    private void checkResourceAccess(String userId) {
        String loggedUserId = userService.getAuthorizedUserId();
        if (!userId.equals(loggedUserId))
            throw new CustomErrorException("message does not belong to current user");
    }

    @Override
    public Page<MessageResponse> getMessagesByChatId(Integer pageNumber, Integer pageSize, String chatId) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        Page<Message> messagePage = messageRepository.findAllByChatId(chatId, pageRequest);
        return messagePage.map(messageMapper::toResponse);
    }

    @Override
    public Optional<MessageResponse> getLastMessageByChat(Chat chat) {
        return messageRepository.findFirstByChatOrderByCreateDateDesc(chat)
                .map(messageMapper::toResponse);
    }

    @Override
    public Optional<MessageResponse> setMessageSeen(String messageId) {
        Message existingMessage = findMessageOrElseThrow(messageId);
        existingMessage.setIsSeen(true);

        Message savedMessage = messageRepository.save(existingMessage);
        return Optional.ofNullable(messageMapper.toResponse(savedMessage));
    }

}