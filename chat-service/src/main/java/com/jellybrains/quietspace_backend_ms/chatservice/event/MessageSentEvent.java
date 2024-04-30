package com.jellybrains.quietspace_backend_ms.chatservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageSentEvent {
    private String senderId;
    private String message;
    private String chatId;
}
