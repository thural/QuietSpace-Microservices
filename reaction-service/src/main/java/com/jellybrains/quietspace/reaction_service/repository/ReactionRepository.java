package com.jellybrains.quietspace.reaction_service.repository;

import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.ReactionType;
import com.jellybrains.quietspace.reaction_service.model.Reaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReactionRepository extends MongoRepository<Reaction, String> {

    Page<Reaction> findAllByContentId(String contentId, PageRequest pageRequest);

    Page<Reaction> findAllByUserId(String userId, PageRequest pageRequest);

    boolean existsByContentIdAndUserId(String contentId, String userId);

    Optional<Reaction> findByContentIdAndUserId(String commentId, String id);

    Page<Reaction> findAllByContentTypeAndUserId(ContentType contentType, String userId, PageRequest pageRequest);

    Page<Reaction> findAllByContentIdAndContentType(String contentId, ContentType contentType, PageRequest pageRequest);

    Page<Reaction> findAllByUserIdAndContentType(String userId, ContentType contentType, PageRequest pageRequest);

    Integer countByContentIdAndReactionType(String contentId, ReactionType reactionType);

    Page<Reaction> findAllByContentIdAndReactionType(String contentId, ReactionType reactionType, PageRequest pageRequest);

    void deleteReactionsByUserId(String userId);
}