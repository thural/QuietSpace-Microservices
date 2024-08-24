package com.jellybrains.quietspace_backend_ms.reaction_service.common.client;



import com.jellybrains.quietspace_backend_ms.reaction_service.common.model.response.ReactionResponse;
import com.jellybrains.quietspace_backend_ms.reaction_service.common.enums.ReactionType;

import java.util.Optional;

public interface ReactionClient {

    Optional<ReactionResponse> getUserReactionByContentId(String contentId);

    Optional<Integer> countByContentIdAndReactionType(String commentId, ReactionType reactionType);
}
