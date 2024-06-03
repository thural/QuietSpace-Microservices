package com.jellybrains.quietspace_backend_ms.reaction_service.model.response;

import com.jellybrains.quietspace_backend_ms.reaction_service.utils.enums.LikeType;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReactionResponse {

    private String id;
    private String userId;
    private String contentId;
    private LikeType likeType;
    private OffsetDateTime updateDate;

}
