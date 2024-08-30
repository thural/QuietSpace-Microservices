package com.jellybrains.quietspace.notification_service.websocket.event.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jellybrains.quietspace.common_service.enums.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseEvent {

    String message;
    Object eventBody;

    @NotNull
    EventType type;

}
