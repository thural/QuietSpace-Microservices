package com.jellybrains.quietspace.common_service.message;

import com.jellybrains.quietspace.common_service.model.response.ProfileResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserProfileEvent extends BaseEvent {
    private ProfileResponse eventBody;
    private String userId;
}

