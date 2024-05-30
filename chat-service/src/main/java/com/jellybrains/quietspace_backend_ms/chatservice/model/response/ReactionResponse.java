package com.jellybrains.quietspace_backend_ms.chatservice.model.response;

import com.jellybrains.quietspace_backend_ms.chatservice.utils.enums.LikeType;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReactionResponse {

    private UUID id;
    private UUID userId;
    private UUID contentId;
    private String username;
    private LikeType likeType;
    private OffsetDateTime updateDate;

}
