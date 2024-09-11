package com.jellybrains.quietspace.chat_service.service.impls;

import com.jellybrains.quietspace.chat_service.entity.Chat;
import com.jellybrains.quietspace.chat_service.mapper.custom.ChatMapper;
import com.jellybrains.quietspace.chat_service.repository.ChatRepository;
import com.jellybrains.quietspace.chat_service.service.ChatService;
import com.jellybrains.quietspace.common_service.exception.CustomNotFoundException;
import com.jellybrains.quietspace.common_service.exception.UnauthorizedException;
import com.jellybrains.quietspace.common_service.model.request.ChatRequest;
import com.jellybrains.quietspace.common_service.model.response.ChatResponse;
import com.jellybrains.quietspace.common_service.webclient.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final UserService userService;


    @Override
    public Flux<ChatResponse> getChatsByUserId(String memberId) {
        if (!userService.getAuthorizedUserId().equals(memberId))
            throw new UnauthorizedException("user mismatch with the chat member");
        return chatRepository.findChatsContainingUserId(memberId)
                .map(chatMapper::chatEntityToResponse);
    }


    @Override
    public Mono<Void> deleteChatById(String chatId) {
        return findChatEntityById(chatId)
                .doOnSuccess(chat -> chatRepository.deleteById(chatId)).then();
    }


    @Override
    public Flux<String> addMemberWithId(String memberId, String chatId) {
        return findChatEntityById(chatId)
                .doOnNext(chat -> userService.validateUserId(memberId))
                .doOnNext(chat -> {
                    chat.getMemberIds().add(memberId);
                })
                .flatMapMany(chat -> Flux.fromIterable(chat.getMemberIds()));
    }


    @Override
    public Flux<String> removeMemberWithId(String memberId, String chatId) {
        userService.validateUserId(memberId);
        return findChatEntityById(chatId)
                .flatMapMany(chat -> {
                    chat.getMemberIds().remove(memberId);
                    return Flux.fromIterable(chat.getMemberIds());
                });
    }


    @Override
    public Mono<ChatResponse> createChat(ChatRequest chatRequest) {
        String userId = userService.getAuthorizedUserId();
        if (!chatRequest.getUserIds().contains(userId))
            throw new UnauthorizedException("user is not a chat member");
        return chatRepository.save(chatMapper.chatRequestToEntity(chatRequest))
                .map(chatMapper::chatEntityToResponse);
    }


    @Override
    public Mono<ChatResponse> getChatById(String chatId) {
        return findChatEntityById(chatId)
                .switchIfEmpty(Mono.error(CustomNotFoundException::new))
                .map(chatMapper::chatEntityToResponse);
    }


    private Mono<Chat> findChatEntityById(String chatId) {
        String authorizedUserId = userService.getAuthorizedUserId();
        return chatRepository.findById(chatId)
                .switchIfEmpty(Mono.error(CustomNotFoundException::new))
                .doOnNext(chat -> {
                    if (!chat.getMemberIds().contains(authorizedUserId))
                        throw new UnauthorizedException("user is not a chat member");
                });
    }


}
