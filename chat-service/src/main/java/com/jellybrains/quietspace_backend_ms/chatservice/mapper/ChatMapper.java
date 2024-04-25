package com.jellybrains.quietspace_backend_ms.chatservice.mapper;

import com.jellybrains.quietspace_backend_ms.chatservice.dto.request.ChatRequest;
import com.jellybrains.quietspace_backend_ms.chatservice.dto.response.ChatResponse;
import com.jellybrains.quietspace_backend_ms.chatservice.model.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ChatMapper {

    @Mapping(target = "id", ignore = true)
    Chat chatRequestToEntity(ChatRequest chatRequest);

    ChatResponse chatEntityToResponse(Chat chat);
}