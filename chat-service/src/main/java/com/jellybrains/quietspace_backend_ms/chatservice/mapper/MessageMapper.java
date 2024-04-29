package com.jellybrains.quietspace_backend_ms.chatservice.mapper;

import com.jellybrains.quietspace_backend_ms.chatservice.dto.request.MessageRequest;
import com.jellybrains.quietspace_backend_ms.chatservice.dto.response.MessageResponse;
import com.jellybrains.quietspace_backend_ms.chatservice.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MessageMapper {
    @Mapping(target = "id", ignore = true)
    Message messageRequestToEntity(MessageRequest messageRequest);

    @Mapping(target = "chatId", source = "chat.id")
    MessageResponse messageEntityToResponse(Message messageEntity);

}
