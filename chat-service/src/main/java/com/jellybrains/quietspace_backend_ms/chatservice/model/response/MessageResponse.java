package com.jellybrains.quietspace_backend_ms.chatservice.model.response;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private String id;
    private String chatId;
    private String text;
    private Boolean isSeen;
    private String senderId;
    private String recipientId;
    private String senderName;
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;

}