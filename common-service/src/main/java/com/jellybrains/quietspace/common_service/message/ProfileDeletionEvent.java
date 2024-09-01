package com.jellybrains.quietspace.common_service.message;

import com.jellybrains.quietspace.common_service.enums.EventType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ProfileDeletionEvent extends UserProfileEvent{
    @Builder.Default
    EventType type = EventType.PROFILE_DELETION_REQUEST;
}
