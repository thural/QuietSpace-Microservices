package com.jellybrains.quietspace.reaction_service.repository;

import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.ReactionType;
import com.jellybrains.quietspace.reaction_service.model.Reaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactionRepository extends ReactiveMongoRepository<Reaction, String> {

    Flux<Reaction> findAllByContentId(String contentId, Pageable pageable);

    Flux<Reaction> findAllByUserId(String userId, Pageable pageable);

    Mono<Boolean> existsByContentIdAndUserId(String contentId, String userId);

    Mono<Reaction> findByContentIdAndUserId(String commentId, String id);

    Flux<Reaction> findAllByContentTypeAndUserId(ContentType contentType, String userId, Pageable pageable);

    Flux<Reaction> findAllByContentIdAndContentType(String contentId, ContentType contentType, Pageable pageable);

    Flux<Reaction> findAllByUserIdAndContentType(String userId, ContentType contentType, Pageable pageable);

    Mono<Integer> countByContentIdAndReactionType(String contentId, ReactionType reactionType);

    Flux<Reaction> findAllByContentIdAndReactionType(String contentId, ReactionType reactionType, Pageable pageable);

    Mono<Void> deleteReactionsByUserId(String userId);
}