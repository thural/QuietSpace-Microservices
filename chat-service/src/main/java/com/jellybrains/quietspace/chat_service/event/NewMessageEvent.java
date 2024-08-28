package com.jellybrains.quietspace.chat_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewMessageEvent {
    private String senderId;
    private String receiverId;
    private String message;
    private String chatId;
}
