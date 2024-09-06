package com.jellybrains.quietspace.common_service.message.kafka.profile;

import com.jellybrains.quietspace.common_service.enums.EventType;
import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDeletionEvent extends KafkaBaseEvent {
    @Builder.Default
    EventType type = EventType.PROFILE_DELETION_REQUEST;
}
