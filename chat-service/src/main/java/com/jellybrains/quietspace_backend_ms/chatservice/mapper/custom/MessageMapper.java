package com.jellybrains.quietspace_backend_ms.chatservice.mapper.custom;

import com.jellybrains.quietspace_backend_ms.chatservice.client.UserClient;
import com.jellybrains.quietspace_backend_ms.chatservice.entity.Message;
import com.jellybrains.quietspace_backend_ms.chatservice.exception.UserNotFoundException;
import com.jellybrains.quietspace_backend_ms.chatservice.model.request.MessageRequest;
import com.jellybrains.quietspace_backend_ms.chatservice.model.response.MessageResponse;
import com.jellybrains.quietspace_backend_ms.chatservice.model.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final UserClient userClient;

    public Message messageRequestToEntity(MessageRequest messageRequest){
        return Message.builder()
                .text(messageRequest.getText())
                .senderId(messageRequest.getSenderId())
                .chat(null)
                .build();
    }

    public MessageResponse messageEntityToDto(Message messageEntity){
        return MessageResponse.builder()
                .id(messageEntity.getId())
                .chatId(messageEntity.getChat().getId())
                .text(messageEntity.getText())
                .senderId(messageEntity.getSenderId())
                .createDate(messageEntity.getCreateDate())
                .updateDate(messageEntity.getUpdateDate())
                .username(getUsernameById(messageEntity.getId()))
                .build();
    }

    private String getUsernameById(UUID userId){
        return userClient.getUserById(userId)
                .map(UserResponse::getUsername)
                .orElseThrow(UserNotFoundException::new);
    }

}
