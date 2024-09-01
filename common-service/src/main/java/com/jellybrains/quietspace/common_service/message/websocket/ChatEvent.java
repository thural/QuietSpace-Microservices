package com.jellybrains.quietspace.common_service.message.websocket;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Getter
@Setter
@SuperBuilder
public class ChatEvent extends BaseEvent {

    private String chatId;
    private String actorId;
    private String messageId;
    private String recipientId;
    private List<String> userIds;

}
