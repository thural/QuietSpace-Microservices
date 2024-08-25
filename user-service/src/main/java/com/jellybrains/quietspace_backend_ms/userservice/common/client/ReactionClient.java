package com.jellybrains.quietspace_backend_ms.userservice.common.client;


import com.jellybrains.quietspace_backend_ms.userservice.common.enums.ReactionType;
import com.jellybrains.quietspace_backend_ms.userservice.common.model.response.ReactionResponse;

import java.util.Optional;

public interface ReactionClient {

    Optional<ReactionResponse> getUserReactionByContentId(String contentId);

    Optional<Integer> countByContentIdAndReactionType(String commentId, ReactionType reactionType);
}
