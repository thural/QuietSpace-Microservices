package com.jellybrains.quietspace_backend_ms.feedservice.common.client;

import com.jellybrains.quietspace_backend_ms.feedservice.common.model.response.ReactionResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.common.utils.enums.ReactionType;

import java.util.Optional;

public interface ReactionClient {

    Optional<ReactionResponse> getUserReactionByContentId(String contentId);

    Optional<Integer> countByContentIdAndReactionType(String commentId, ReactionType reactionType);
}
