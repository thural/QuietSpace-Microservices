package com.jellybrains.quietspace.common_service.message.kafka.profile;

import com.jellybrains.quietspace.common_service.enums.EventType;
import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCreationEventFailed extends KafkaBaseEvent {
    @Builder.Default
    EventType type = EventType.PROFILE_CREATION_FAILED;
}
