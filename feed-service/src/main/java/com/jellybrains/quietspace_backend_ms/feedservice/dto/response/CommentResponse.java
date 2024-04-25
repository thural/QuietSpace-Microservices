package com.jellybrains.quietspace_backend_ms.feedservice.dto.response;

import lombok.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private UUID id;
    private UUID userId;
    private UUID postId;
    private String username;
    private String text;
    private List<CommentLikeResponse> likes;
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;

}
