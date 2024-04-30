package com.jellybrains.quietspace_backend_ms.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageSentEvent {
    private String senderId;
    private String message;
    private String chatId;
}
