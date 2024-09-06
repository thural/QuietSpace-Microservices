package com.jellybrains.quietspace.common_service.message.kafka.chat.request;

import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
public class SeenMessageRequest extends KafkaBaseEvent {
    String messageId;
}
