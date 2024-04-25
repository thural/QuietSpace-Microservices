package com.jellybrains.quietspace_backend_ms.chatservice.dto.response;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponse {

    private UUID id;
    private List<UUID> userIds;
    private List<MessageResponse> messages;
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;

}
