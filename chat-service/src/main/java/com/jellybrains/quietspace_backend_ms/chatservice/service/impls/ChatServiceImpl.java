package com.jellybrains.quietspace_backend_ms.chatservice.service.impls;

import com.jellybrains.quietspace_backend_ms.chatservice.client.UserClient;
import com.jellybrains.quietspace_backend_ms.chatservice.common.UserService;
import com.jellybrains.quietspace_backend_ms.chatservice.entity.Chat;
import com.jellybrains.quietspace_backend_ms.chatservice.exception.CustomErrorException;
import com.jellybrains.quietspace_backend_ms.chatservice.exception.UnauthorizedException;
import com.jellybrains.quietspace_backend_ms.chatservice.mapper.custom.ChatMapper;
import com.jellybrains.quietspace_backend_ms.chatservice.model.request.ChatRequest;
import com.jellybrains.quietspace_backend_ms.chatservice.model.response.ChatResponse;
import com.jellybrains.quietspace_backend_ms.chatservice.repository.ChatRepository;
import com.jellybrains.quietspace_backend_ms.chatservice.service.ChatService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final UserService userService;
    private final UserClient userClient;


    @Override
    public List<ChatResponse> getChatsByUserId(String memberId) {

        if (!userService.getAuthorizedUserId().equals(memberId))
            throw new UnauthorizedException("user mismatch with the chat member");

        return chatRepository.findAllByUsersId(memberId)
                .stream()
                .map(chatMapper::chatEntityToResponse)
                .toList();
    }


    @Override
    public void deleteChatById(String chatId) {
        findChatEntityById(chatId);
        chatRepository.deleteById(chatId);
    }


    @Override
    public void addMemberWithId(String memberId, String chatId) {
        
        Chat foundChat = findChatEntityById(chatId);

        userService.validateUserId(memberId);
        List<String> users = foundChat.getUsers();
        users.add(memberId);
        foundChat.setUsers(users);

        chatRepository.save(foundChat);
    }


    @Override
    public List<String> removeMemberWithId(String memberId, String chatId) {
        Chat foundChat = findChatEntityById(chatId);
        List<String> members = foundChat.getUsers();
        userService.validateUserId(memberId);
        members.remove(memberId);
        foundChat.setUsers(members);
        chatRepository.save(foundChat);
        return members;
    }


    @Override
    public ChatResponse createChat(ChatRequest chatRequest) {

        List<String> userList = chatRequest.getUserIds();
        String userId = userService.getAuthorizedUserId();

        if (!userList.contains(userId))
            throw new UnauthorizedException("requesting user is not member of requested chat");

        boolean isChatDuplicate = chatRepository.findAllByUsersIn(userList).stream()
                .anyMatch(chat -> new HashSet<>(chat.getUsers()).containsAll(userList));

        if (isChatDuplicate) throw new CustomErrorException("a chat with same members already exists");

        Chat createdChat = chatRepository.save(chatMapper.chatRequestToEntity(chatRequest));
        return chatMapper.chatEntityToResponse(createdChat);

    }


    @Override
    public ChatResponse getChatById(String chatId) {
        Chat foundChat = findChatEntityById(chatId);
        return chatMapper.chatEntityToResponse(foundChat);
    }


    private Chat findChatEntityById(String chatId) {
        String loggedUser = userService.getAuthorizedUserId();

        Chat foundChat = chatRepository.findById(chatId)
                .orElseThrow(EntityNotFoundException::new);

        if (!foundChat.getUsers().contains(loggedUser))
            throw new UnauthorizedException("chat does not belong to logged user");

        return foundChat;
    }

}
