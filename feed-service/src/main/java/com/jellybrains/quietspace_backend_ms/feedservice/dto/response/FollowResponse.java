package com.jellybrains.quietspace_backend_ms.feedservice.dto.response;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowResponse {

    private UUID id;
    private UUID followingId;
    private UUID followerId;
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;

}
