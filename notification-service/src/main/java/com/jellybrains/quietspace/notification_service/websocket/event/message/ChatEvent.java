package com.jellybrains.quietspace.notification_service.websocket.event.message;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
public class ChatEvent extends BaseEvent {

    private String chatId;
    private String actorId;
    private String messageId;
    private String recipientId;

}
