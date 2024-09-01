package com.jellybrains.quietspace.common_service.message.kafka;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jellybrains.quietspace.common_service.enums.EventType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KafkaBaseEvent {

    String message;
    Object eventBody;
    EventType type;

}
