package com.jellybrains.quietspace.common_service.model.response;

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
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;

}
