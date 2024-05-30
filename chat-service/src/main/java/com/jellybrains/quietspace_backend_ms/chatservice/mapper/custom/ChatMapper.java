package com.jellybrains.quietspace_backend_ms.chatservice.mapper.custom;

import com.jellybrains.quietspace_backend_ms.chatservice.client.UserClient;
import com.jellybrains.quietspace_backend_ms.chatservice.entity.Chat;

import com.jellybrains.quietspace_backend_ms.chatservice.model.request.ChatRequest;
import com.jellybrains.quietspace_backend_ms.chatservice.model.response.ChatResponse;
import com.jellybrains.quietspace_backend_ms.chatservice.model.response.MessageResponse;
import com.jellybrains.quietspace_backend_ms.chatservice.model.response.UserResponse;
import com.jellybrains.quietspace_backend_ms.chatservice.service.MessageService;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatMapper {

    private final UserClient userClient;
    private final MessageService messageService;

    public Chat chatRequestToEntity(ChatRequest chatRequest){
        return Chat.builder()
                .userIds(getUserIdListFromRequest(chatRequest))
                .build();
    }

    public ChatResponse chatEntityToResponse(Chat chat){
        return ChatResponse.builder()
                .id(chat.getId())
                .userIds(chat.getUserIds().stream().toList())
                .members(getChatMembers(chat))
                .recentMessage(getLastMessage(chat))
                .createDate(chat.getCreateDate())
                .updateDate(chat.getUpdateDate())
                .build();
    }

    private MessageResponse getLastMessage(Chat chat){
        return messageService.getLastMessageByChat(chat).orElse(null);
    }

    private Set<UUID> getUserIdListFromRequest(ChatRequest chatRequest){
        if(!userClient.validateUserIdList(chatRequest.getUserIds()))
            throw new BadRequestException("user list is invalid");

        return new HashSet<>(chatRequest.getUserIds());
    }

    private List<UserResponse> getChatMembers(Chat chat){

        UserResponse loggedUser = userClient.getLoggedUser()
                .orElseThrow(BadRequestException::new);

        return chat.getUserIds().stream()
                .map(userClient::getUserById)
                .map(userResponse -> userResponse.orElseThrow(BadRequestException::new))
                .filter(userResponse -> !userResponse.getId().equals(loggedUser.getId()))
                .toList();
    }

}
