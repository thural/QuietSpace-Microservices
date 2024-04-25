package com.jellybrains.quietspace_backend_ms.chatservice.dto.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequest {

    @NonNull
    private UUID chatId;
    @NonNull
    private UUID senderId;
    @NonNull
    private String text;

}
