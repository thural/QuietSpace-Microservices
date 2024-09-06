package com.jellybrains.quietspace.common_service.message.kafka.chat.request;

import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
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
public class DeleteMessageRequest extends KafkaBaseEvent {
    String messageId;
}
