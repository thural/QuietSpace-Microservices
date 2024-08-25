package com.jellybrains.quietspace_backend_ms.userservice.common.model.response;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private String id;
    private String userId;
    private String postId;
    private String text;
    private String parentId;
    private String username;
    private Integer likeCount;
    private Integer replyCount;
    private ReactionResponse userReaction;
    private OffsetDateTime createDate = OffsetDateTime.now();
    private OffsetDateTime updateDate = OffsetDateTime.now();

}
