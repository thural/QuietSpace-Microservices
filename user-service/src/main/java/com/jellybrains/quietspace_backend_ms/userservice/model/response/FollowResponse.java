package com.jellybrains.quietspace_backend_ms.userservice.model.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowResponse {

    private UUID followingId;
    private UUID followerId;

}
