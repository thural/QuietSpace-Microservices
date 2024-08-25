package com.jellybrains.quietspace_backend_ms.feedservice.model.response;

import com.jellybrains.quietspace.common_service.model.response.ReactionResponse;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private String id;
    private String userId;
    private String username;
    private String title;
    private String text;
    private PollResponse poll;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer commentCount;
    private ReactionResponse userReaction;
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;

}