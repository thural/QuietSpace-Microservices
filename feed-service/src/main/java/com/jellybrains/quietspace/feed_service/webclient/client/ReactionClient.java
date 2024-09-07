package com.jellybrains.quietspace.feed_service.webclient.client;

import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.ReactionType;
import com.jellybrains.quietspace.common_service.model.response.ReactionResponse;

import java.util.Optional;

public interface ReactionClient {

    Optional<String> sayHello();

    Optional<ReactionResponse> getUserReactionByContentId(String contentId, ContentType type);

    Optional<Integer> countByContentIdAndReactionType(String commentId, ReactionType reactionType);
}
