package com.jellybrains.quietspace_backend_ms.reaction_service.common.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jellybrains.quietspace_backend_ms.reaction_service.common.enums.EventType;
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
