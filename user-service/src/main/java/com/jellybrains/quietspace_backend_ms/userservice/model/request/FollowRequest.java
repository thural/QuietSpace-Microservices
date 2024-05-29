package com.jellybrains.quietspace_backend_ms.userservice.model.request;

import dev.thural.quietspace.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowRequest {

    @NotNull
    private User followingId;

    @NotNull
    private User followerId;

}
