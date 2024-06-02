package com.jellybrains.quietspace_backend_ms.chatservice.service.impls;

import com.jellybrains.quietspace_backend_ms.chatservice.client.UserClient;
import com.jellybrains.quietspace_backend_ms.chatservice.entity.Chat;
import com.jellybrains.quietspace_backend_ms.chatservice.exception.UnauthorizedException;
import com.jellybrains.quietspace_backend_ms.chatservice.exception.UserNotFoundException;
import com.jellybrains.quietspace_backend_ms.chatservice.mapper.custom.ChatMapper;
import com.jellybrains.quietspace_backend_ms.chatservice.model.request.ChatRequest;
import com.jellybrains.quietspace_backend_ms.chatservice.model.response.ChatResponse;
import com.jellybrains.quietspace_backend_ms.chatservice.model.response.UserResponse;
import com.jellybrains.quietspace_backend_ms.chatservice.repository.ChatRepository;
import com.jellybrains.quietspace_backend_ms.chatservice.service.ChatService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final UserClient userClient;


    @Override
    public List<ChatResponse> getChatsByUserId(UUID memberId) {

        if (!userClient.validateUserId(memberId))
            throw new BadRequestException("user mismatch with requested chat member");

        return chatRepository.findAllByUserIdsContaining(memberId)
                .stream()
                .map(chatMapper::chatEntityToResponse)
                .toList();

    }


    @Override
    public void deleteChatById(UUID chatId) {
        getChatById(chatId);
        if (chatRepository.existsById(chatId)) chatRepository.deleteById(chatId);
    }


    @Override
    public ChatResponse addMemberWithId(UUID memberId, UUID chatId) {

        Chat foundChat = findChatById(chatId);
        UserResponse foundMember = getUserById(memberId);

        Set<UUID> memberIds = foundChat.getUserIds();
        memberIds.add(foundMember.getId());
        foundChat.setUserIds(memberIds);
        Chat patchedChat = chatRepository.save(foundChat);

        return chatMapper.chatEntityToResponse(patchedChat);

    }


    @Override
    public void removeMemberWithId(UUID memberId, UUID chatId) {

        Chat foundChat = findChatById(chatId);
        UserResponse foundMember = getUserById(memberId);

        Set<UUID> members = foundChat.getUserIds();
        members.remove(foundMember.getId());
        foundChat.setUserIds(members);
        chatRepository.save(foundChat);

    }


    @Override
    public ChatResponse createChat(ChatRequest chatRequest) {

        if (!chatRequest.getUserIds().contains(getLoggedUser().getId()))
            throw new UnauthorizedException("user is not member of requested chat");

        Chat createdChat = chatRepository.save(chatMapper.chatRequestToEntity(chatRequest));
        return chatMapper.chatEntityToResponse(createdChat);

    }


    @Override
    public ChatResponse getChatById(UUID chatId) {
        Chat foundChat = findChatById(chatId);
        return chatMapper.chatEntityToResponse(foundChat);
    }


    private UserResponse getUserById(UUID memberId) {
        return userClient.getUserById(memberId)
                .orElseThrow(() -> new UserNotFoundException("user not found"));
    }


    private Chat findChatById(UUID chatId) {

        Chat foundChat = chatRepository.findById(chatId)
                .orElseThrow(EntityNotFoundException::new);

        if (!foundChat.getUserIds().contains(getLoggedUser().getId()))
            throw new BadRequestException("requesting user is not a member of this chat");

        return foundChat;

    }

    private UserResponse getLoggedUser(){
        return userClient.getLoggedUser().orElseThrow(UserNotFoundException::new);
    }

}
