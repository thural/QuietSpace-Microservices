package com.jellybrains.quietspace_backend_ms.authorization_service.event.creation;

import com.jellybrains.quietspace_backend_ms.authorization_service.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationFailedEvent implements BaseEvent {
    private UUID userId;
}
