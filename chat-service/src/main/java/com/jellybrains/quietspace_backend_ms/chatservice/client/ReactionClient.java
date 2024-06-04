package com.jellybrains.quietspace_backend_ms.chatservice.client;

import com.jellybrains.quietspace_backend_ms.chatservice.model.response.ReactionResponse;

import java.util.Optional;
import java.util.UUID;

public interface ReactionClient {

    Integer getLikeCountByContentId(UUID contentId);

    Integer getDislikeCountByContentId(UUID contentId);

    Optional<ReactionResponse> getUserReactionByContentId(UUID contentId);

}
