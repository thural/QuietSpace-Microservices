package com.jellybrains.quietspace_backend_ms.userservice.common.message;

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
