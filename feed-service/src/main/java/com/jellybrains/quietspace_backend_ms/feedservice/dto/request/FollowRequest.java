package com.jellybrains.quietspace_backend_ms.feedservice.dto.request;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowRequest {

    @NonNull
    private UUID followingId;
    @NonNull
    private UUID followerId;

}
