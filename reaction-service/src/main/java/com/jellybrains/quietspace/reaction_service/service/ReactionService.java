package com.jellybrains.quietspace.reaction_service.service;

import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.ReactionType;
import com.jellybrains.quietspace.reaction_service.model.Reaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactionService {

    Mono<Void> handleReaction(Reaction reaction);

    Mono<Reaction> getUserReactionByContentId(String contentId);

    Flux<Reaction> getReactionsByContentIdAndType(String contentId, ReactionType reactionType, Integer pageNumber, Integer pageSize);

    Mono<Integer> countByContentIdAndType(String contentId, ReactionType reactionType);

    Flux<Reaction> getAllByContentIdAndContentType(String contentId, ContentType type, Integer pageNumber, Integer pageSize);

    Flux<Reaction> getReactionsByUserIdAndContentType(String userId, ContentType contentType, Integer pageNumber, Integer pageSize);

}
