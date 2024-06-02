package com.jellybrains.quietspace_backend_ms.userservice.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowRequest {

    @NotNull(message = "following id can not be null")
    private UUID followingId;

    @NotNull(message = "follower id can not be null")
    private UUID followerId;

}
