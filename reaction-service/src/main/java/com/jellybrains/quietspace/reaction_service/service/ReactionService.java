package com.jellybrains.quietspace.reaction_service.service;

import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.ReactionType;
import com.jellybrains.quietspace.reaction_service.model.Reaction;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ReactionService {

    void handleReaction(Reaction reaction);

    Optional<Reaction> getUserReactionByContentId(String contentId);

    Page<Reaction> getReactionsByContentIdAndType(String contentId, ReactionType reactionType, Integer pageNumber, Integer pageSize);

    Integer countByContentIdAndType(String contentId, ReactionType reactionType);

    Page<Reaction> getReactionsByContentIdAndContentType(String contentId, ContentType type, Integer pageNumber, Integer pageSize);

    Page<Reaction> getReactionsByUserIdAndContentType(String userId, ContentType contentType, Integer pageNumber, Integer pageSize);

}
