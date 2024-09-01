package com.jellybrains.quietspace.common_service.message.kafka.notification;

import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
public class NotificationEvent extends KafkaBaseEvent {

    private String actorId;
    private String notificationId;
    private String recipientId;

}
