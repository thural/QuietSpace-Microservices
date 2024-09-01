package com.jellybrains.quietspace.common_service.message.kafka.chat.event;

import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import com.jellybrains.quietspace.common_service.model.response.MessageResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
public class SendMessageEvent extends KafkaBaseEvent {
    String userId;
    MessageResponse eventBody;
}
