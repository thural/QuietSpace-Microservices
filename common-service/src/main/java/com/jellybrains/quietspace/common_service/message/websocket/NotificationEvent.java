package com.jellybrains.quietspace.common_service.message.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent extends BaseEvent {

    private String actorId;
    private String notificationId;
    private String recipientId;

}
