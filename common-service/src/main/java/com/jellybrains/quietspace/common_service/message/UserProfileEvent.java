package com.jellybrains.quietspace.common_service.message;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserProfileEvent extends BaseEvent {
    private String userId;
}

