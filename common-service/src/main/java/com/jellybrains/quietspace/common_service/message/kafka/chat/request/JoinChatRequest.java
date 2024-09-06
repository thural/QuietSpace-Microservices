package com.jellybrains.quietspace.common_service.message.kafka.chat.request;

import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import com.jellybrains.quietspace.common_service.message.websocket.ChatEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
public class JoinChatRequest extends KafkaBaseEvent {
    ChatEvent eventBody;
}
