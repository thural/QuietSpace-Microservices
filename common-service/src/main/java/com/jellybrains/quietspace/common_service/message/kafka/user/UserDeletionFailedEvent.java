package com.jellybrains.quietspace.common_service.message.kafka.user;

import com.jellybrains.quietspace.common_service.enums.EventType;
import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDeletionFailedEvent extends KafkaBaseEvent {
    @Builder.Default
    EventType type = EventType.USER_DELETION_FAILED;
    String userId;
}
