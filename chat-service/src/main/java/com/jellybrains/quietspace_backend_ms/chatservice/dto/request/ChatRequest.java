package com.jellybrains.quietspace_backend_ms.chatservice.dto.request;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRequest {

    @NonNull
    private List<UUID> userIds;
    @NonNull
    private MessageRequest message;

}
