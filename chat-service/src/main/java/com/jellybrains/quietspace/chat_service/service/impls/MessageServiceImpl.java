package com.jellybrains.quietspace.chat_service.service.impls;

import com.jellybrains.quietspace.chat_service.entity.Chat;
import com.jellybrains.quietspace.chat_service.entity.Message;
import com.jellybrains.quietspace.chat_service.mapper.custom.MessageMapper;
import com.jellybrains.quietspace.chat_service.repository.ChatRepository;
import com.jellybrains.quietspace.chat_service.repository.MessageRepository;
import com.jellybrains.quietspace.chat_service.service.MessageService;
import com.jellybrains.quietspace.common_service.exception.CustomErrorException;
import com.jellybrains.quietspace.common_service.exception.CustomNotFoundException;
import com.jellybrains.quietspace.common_service.model.request.MessageRequest;
import com.jellybrains.quietspace.common_service.model.response.MessageResponse;
import com.jellybrains.quietspace.common_service.service.shared.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    @TimeLimiter(name = "chat-service")
    @CircuitBreaker(name = "chat-service")
    public Mono<MessageResponse> addMessage(MessageRequest messageRequest) {
        String authorizedUserId = userService.getAuthorizedUserId();
        return chatRepository.findById(messageRequest.getChatId())
                .switchIfEmpty(Mono.error(CustomNotFoundException::new))
                .flatMap(chat -> {
                    Message message = messageMapper.toEntity(messageRequest);
                    message.setSenderId(authorizedUserId);
                    message.setChat(chat);
                    return messageRepository.save(message);
                }).map(messageMapper::toResponse);
    }

    @Override
    @TimeLimiter(name = "chat-service")
    @CircuitBreaker(name = "chat-service")
    public Mono<MessageResponse> deleteMessage(String messageId) {
        return findMessageOrElseThrow(messageId)
                .switchIfEmpty(Mono.error(CustomNotFoundException::new))
                .doOnNext(message -> checkResourceAccess(message.getSenderId()))
                .doOnNext(message -> messageRepository.deleteById(messageId))
                .map(messageMapper::toResponse);
    }

    @TimeLimiter(name = "chat-service")
    @CircuitBreaker(name = "chat-service")
    private Mono<Message> findMessageOrElseThrow(String messageId) {
        return messageRepository.findById(messageId)
                .switchIfEmpty(Mono.error(CustomNotFoundException::new));
    }

    @TimeLimiter(name = "chat-service")
    @CircuitBreaker(name = "chat-service")
    private void checkResourceAccess(String userId) {
        String loggedUserId = userService.getAuthorizedUserId();
        if (!userId.equals(loggedUserId))
            throw new CustomErrorException("message does not belong to current user");
    }

    @Override
    @TimeLimiter(name = "chat-service")
    @CircuitBreaker(name = "chat-service")
    public Flux<MessageResponse> getMessagesByChatId(Integer pageNumber, Integer pageSize, String chatId) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        return messageRepository.findAllByChatId(chatId, pageRequest).map(messageMapper::toResponse);
    }

    @Override
    @TimeLimiter(name = "chat-service")
    @CircuitBreaker(name = "chat-service")
    public Mono<MessageResponse> getLastMessageByChat(Chat chat) {
        return messageRepository.findFirstByChatOrderByCreateDateDesc(chat)
                .map(messageMapper::toResponse);
    }

    @Override
    @TimeLimiter(name = "chat-service")
    @CircuitBreaker(name = "chat-service")
    public Mono<MessageResponse> setMessageSeen(String messageId) {
        return findMessageOrElseThrow(messageId)
                .doOnNext(message -> message.setIsSeen(true))
                .map(messageMapper::toResponse);
    }

}