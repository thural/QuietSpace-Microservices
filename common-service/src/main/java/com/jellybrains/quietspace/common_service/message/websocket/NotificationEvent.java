package com.jellybrains.quietspace.common_service.message.websocket;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
public class NotificationEvent extends BaseEvent {

    private String actorId;
    private String notificationId;
    private String recipientId;

}
