package com.jellybrains.quietspace_backend_ms.userservice.model.request;

import com.jellybrains.quietspace_backend_ms.userservice.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowRequest {

    @NotNull
    private UUID followingId;

    @NotNull
    private UUID followerId;

}
