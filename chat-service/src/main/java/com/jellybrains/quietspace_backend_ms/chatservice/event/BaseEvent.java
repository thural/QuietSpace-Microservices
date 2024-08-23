package com.jellybrains.quietspace_backend_ms.chatservice.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jellybrains.quietspace_backend_ms.chatservice.utils.enums.EventType;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseEvent {

    String message;
    Object eventBody;

    @NotNull
    EventType type;

}
