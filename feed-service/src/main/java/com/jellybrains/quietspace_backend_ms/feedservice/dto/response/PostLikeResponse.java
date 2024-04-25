package com.jellybrains.quietspace_backend_ms.feedservice.dto.response;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeResponse {

    private UUID id;
    private String username;
    private UUID userId;
    private UUID postId;
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;

}
