package com.jellybrains.quietspace.notification_service.websocket.event.message;

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
