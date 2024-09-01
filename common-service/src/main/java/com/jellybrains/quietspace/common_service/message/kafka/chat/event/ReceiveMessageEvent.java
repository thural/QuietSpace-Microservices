package com.jellybrains.quietspace.common_service.message.kafka.chat.event;

import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
public class ReceiveMessageEvent extends KafkaBaseEvent {
    String userId;
}
