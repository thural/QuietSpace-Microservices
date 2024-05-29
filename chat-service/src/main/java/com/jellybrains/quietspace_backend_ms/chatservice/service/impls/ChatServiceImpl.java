package com.jellybrains.quietspace_backend_ms.chatservice.service.impls;

import com.jellybrains.quietspace_backend_ms.chatservice.entity.Chat;
import com.jellybrains.quietspace_backend_ms.chatservice.exception.UnauthorizedException;
import com.jellybrains.quietspace_backend_ms.chatservice.exception.UserNotFoundException;
import com.jellybrains.quietspace_backend_ms.chatservice.model.response.ChatResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;


    @Override
    public List<ChatResponse> getChatsByUserId(UUID memberId) {
        User loggedUser = userService.getLoggedUser();

        if (!loggedUser.getId().equals(memberId))
            throw new UnauthorizedException("user mismatch with the chat member");

        return chatRepository.findAllByUsersId(memberId)
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
        User foundMember = userService.getUserById(memberId)
                .orElseThrow(() -> new UserNotFoundException("user not found"));
        List<User> members = foundChat.getUsers();

        members.add(foundMember);
        foundChat.setUsers(members);
        Chat patchedChat = chatRepository.save(foundChat);

        return chatMapper.chatEntityToResponse(patchedChat);
    }


    @Override
    public void removeMemberWithId(UUID memberId, UUID chatId) {

        Chat foundChat = findChatById(chatId);
        User foundMember = getUserById(memberId);
        List<User> members = foundChat.getUsers();

        members.remove(foundMember);
        foundChat.setUsers(members);
        chatRepository.save(foundChat);

    }


    @Override
    public ChatResponse createChat(ChatRequest chatRequest) {

        List<User> userList = userService.getUsersFromIdList(chatRequest.getUserIds());
        User loggedUser = userService.getLoggedUser();

        if (!userList.contains(loggedUser))
            throw new UnauthorizedException("requesting user is not member of requested chat");

        boolean isChatDuplicate = chatRepository.findAllByUsersIn(userList).stream()
                .anyMatch(chat -> new HashSet<>(chat.getUsers()).containsAll(userList));

        if (isChatDuplicate) throw new CustomErrorException("a chat with same members already exists");

        Chat createdChat = chatRepository.save(chatMapper.chatRequestToEntity(chatRequest));
        return chatMapper.chatEntityToResponse(createdChat);

    }


    @Override
    public ChatResponse getChatById(UUID chatId) {
        Chat foundChat = findChatById(chatId);
        return chatMapper.chatEntityToResponse(foundChat);
    }


    private User getUserById(UUID memberId) {
        return userService.getUserById(memberId)
                .orElseThrow(() -> new UserNotFoundException("user not found"));
    }


    private Chat findChatById(UUID chatId) {
        User loggedUser = userService.getLoggedUser();

        Chat foundChat = chatRepository.findById(chatId)
                .orElseThrow(EntityNotFoundException::new);

        if (!foundChat.getUsers().contains(loggedUser))
            throw new UnauthorizedException("chat does not belong to logged user");

        return foundChat;
    }

}
