package com.jellybrains.quietspace_backend_ms.chatservice.service.impls;

import com.jellybrains.quietspace_backend_ms.chatservice.dto.request.ChatRequest;
import com.jellybrains.quietspace_backend_ms.chatservice.dto.response.ChatResponse;
import com.jellybrains.quietspace_backend_ms.chatservice.mapper.ChatMapper;
import com.jellybrains.quietspace_backend_ms.chatservice.mapper.MessageMapper;
import com.jellybrains.quietspace_backend_ms.chatservice.model.Chat;
import com.jellybrains.quietspace_backend_ms.chatservice.model.Message;
import com.jellybrains.quietspace_backend_ms.chatservice.repository.ChatRepository;
import com.jellybrains.quietspace_backend_ms.chatservice.service.ChatService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    // TODO: implement webflux method to get users
    private final MessageMapper messageMapper;
    private final ChatMapper chatMapper;


    @Override
    public List<ChatResponse> getChatsByUserId(UUID memberId) {
        // TODO: throw an error for mismatching user request

        return chatRepository.findAllByUsersId(memberId)
                .stream()
                .map(chatMapper::chatEntityToResponse).toList();
    }

    @Override
    public void deleteChatById(UUID chatId) {
        getChatById(chatId);
        if (chatRepository.existsById(chatId)) chatRepository.deleteById(chatId);
    }

    @Override
    public void addMemberWithId(UUID memberId, UUID chatId) {
        Chat foundChat = findChatById(chatId);

        checkUserValidity(memberId);

        List<UUID> members = foundChat.getUserList();

        members.add(memberId);
        foundChat.setUserList(members);
        chatRepository.save(foundChat);
    }

    @Override
    public void removeMemberWithId(UUID memberId, UUID chatId) {
        Chat foundChat = findChatById(chatId);

        checkUserValidity(memberId);
        List<UUID> members = foundChat.getUserList();

        members.remove(memberId);
        foundChat.setUserList(members);
        chatRepository.save(foundChat);
    }

    @Override
    public ChatResponse createChat(ChatRequest chatRequest) {
        checkUserValidity(chatRequest.getUserIds().get(1));

        //TODO: check if chat is not duplicate or else throw error

        Chat newChat = chatMapper.chatRequestToEntity(chatRequest);
        newChat.setUserList(chatRequest.getUserIds());
        Message message = messageMapper.messageRequestToEntity(chatRequest.getMessage());
        newChat.setMessages(List.of(message));

        return chatMapper.chatEntityToResponse(chatRepository.save(newChat));
    }

    @Override
    public ChatResponse getChatById(UUID chatId) {
        UUID loggedUserId = getLoggedUserId();

        Chat foundChat = chatRepository.findById(chatId)
                .orElseThrow(EntityNotFoundException::new);

        // TODO: check if logged user id present in chat members or else throw

        return chatMapper.chatEntityToResponse(foundChat);
    }

    private Chat findChatById(UUID chatId) {
        UUID loggedUserId = getLoggedUserId();

        Chat foundChat = chatRepository.findById(chatId)
                .orElseThrow(EntityNotFoundException::new);

        // TODO: check if logged user id present in chat members or else throw

        return foundChat;
    }

    private UUID getLoggedUserId() {
        return null; // TODO: get logged user id
    }

    private void checkUserValidity(UUID userId){
        // TODO: check if user exists or else throw error
    }

}
