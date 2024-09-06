package com.jellybrains.quietspace.common_service.message.kafka.user;

import com.jellybrains.quietspace.common_service.message.kafka.KafkaBaseEvent;
import com.jellybrains.quietspace.common_service.websocket.model.UserRepresentation;
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
public class UserCreationEvent extends KafkaBaseEvent {

    String userId;
    String message;
    UserRepresentation eventBody;

}
