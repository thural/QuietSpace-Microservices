package com.jellybrains.quietspace.common_service.message.kafka.notification;

import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.NotificationType;
import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
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
public class NotificationEvent extends KafkaBaseEvent {

    private String actorId;
    private String notificationId;
    private String recipientId;
    private String contentId;
    private ContentType contentType;
    private NotificationType notificationType;

}
