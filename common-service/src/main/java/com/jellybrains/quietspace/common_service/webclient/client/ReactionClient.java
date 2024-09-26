package com.jellybrains.quietspace.common_service.webclient.client;

import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.ReactionType;
import com.jellybrains.quietspace.common_service.model.response.ReactionResponse;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ReactionClient {

    CompletableFuture<Optional<String>> sayHello();

    CompletableFuture<Optional<ReactionResponse>> getUserReactionByContentId(String contentId, ContentType type);
    
    CompletableFuture<Optional<Integer>> countByContentIdAndReactionType(String commentId, ReactionType reactionType);
}
