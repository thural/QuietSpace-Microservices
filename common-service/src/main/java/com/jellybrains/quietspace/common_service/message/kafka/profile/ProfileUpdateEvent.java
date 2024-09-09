package com.jellybrains.quietspace.common_service.message.kafka.profile;

import com.jellybrains.quietspace.common_service.enums.EventType;
import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import com.jellybrains.quietspace.common_service.websocket.model.UserRepresentation;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateEvent extends KafkaBaseEvent {
    @Builder.Default
    EventType type = EventType.PROFILE_UPDATE_REQUEST_EVENT;
    UserRepresentation eventBody;
}
