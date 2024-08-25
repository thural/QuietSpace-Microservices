package com.jellybrains.quietspace.common_service.client;



import com.jellybrains.quietspace.common_service.enums.ReactionType;
import com.jellybrains.quietspace.common_service.model.response.ReactionResponse;

import java.util.Optional;

public interface ReactionClient {

    Optional<ReactionResponse> getUserReactionByContentId(String contentId);

    Optional<Integer> countByContentIdAndReactionType(String commentId, ReactionType reactionType);
}
