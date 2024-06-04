package com.jellybrains.quietspace_backend_ms.feedservice.client;

import com.jellybrains.quietspace_backend_ms.feedservice.model.response.ReactionResponse;

import java.util.Optional;
import java.util.UUID;

public interface ReactionClient {

    Integer getLikeCountByContentId(UUID contentId);

    Integer getDislikeCountByContentId(UUID contentId);

    Optional<ReactionResponse> getUserReactionByContentId(UUID contentId);

}
