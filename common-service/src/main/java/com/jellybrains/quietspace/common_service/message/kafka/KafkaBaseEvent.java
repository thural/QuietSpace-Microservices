package com.jellybrains.quietspace.common_service.message.kafka;

import com.jellybrains.quietspace.common_service.enums.EventType;
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
public class KafkaBaseEvent {

    String userId;
    String message;
    Object eventBody;
    EventType type;

}
