package com.jellybrains.quietspace_backend_ms.chatservice.dto.response;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {

    private UUID id;
    private String text;
    private UUID chatId;
    private UUID senderId;
    private String username;
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;

}
