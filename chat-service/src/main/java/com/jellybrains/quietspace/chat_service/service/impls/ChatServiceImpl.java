package com.jellybrains.quietspace.chat_service.service.impls;

import com.jellybrains.quietspace.chat_service.entity.Chat;
import com.jellybrains.quietspace.chat_service.mapper.custom.ChatMapper;
import com.jellybrains.quietspace.chat_service.repository.ChatRepository;
import com.jellybrains.quietspace.chat_service.service.ChatService;
import com.jellybrains.quietspace.common_service.exception.UnauthorizedException;
import com.jellybrains.quietspace.common_service.model.request.ChatRequest;
import com.jellybrains.quietspace.common_service.model.response.ChatResponse;
import com.jellybrains.quietspace.common_service.webclient.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final UserService userService;


    @Override
    public List<ChatResponse> getChatsByUserId(String memberId) {

        if (!userService.getAuthorizedUserId().equals(memberId))
            throw new UnauthorizedException("user mismatch with the chat member");

//        return chatRepository.findByUsersContainingUserId(memberId)
//                .stream()
//                .map(chatMapper::chatEntityToResponse)
//                .toList();
        return List.of();
    }


    @Override
    public void deleteChatById(String chatId) {
        findChatEntityById(chatId);
        chatRepository.deleteById(chatId);
    }


    @Override
    public List<String> addMemberWithId(String memberId, String chatId) {

        Chat foundChat = findChatEntityById(chatId);

        userService.validateUserId(memberId);
        List<String> users = foundChat.getMemberIds();
        users.add(memberId);
        foundChat.setMemberIds(users);

        chatRepository.save(foundChat);
        return users;
    }


    @Override
    public List<String> removeMemberWithId(String memberId, String chatId) {
        Chat foundChat = findChatEntityById(chatId);
        List<String> members = foundChat.getMemberIds();
        userService.validateUserId(memberId);
        members.remove(memberId);
        foundChat.setMemberIds(members);
        chatRepository.save(foundChat);
        return members;
    }


    @Override
    public ChatResponse createChat(ChatRequest chatRequest) {

        List<String> userList = chatRequest.getUserIds();
        String userId = userService.getAuthorizedUserId();

        if (!userList.contains(userId))
            throw new UnauthorizedException("requesting user is not member of requested chat");

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

        if (!foundChat.getMemberIds().contains(loggedUser))
            throw new UnauthorizedException("chat does not belong to logged user");

        return foundChat;
    }

}
