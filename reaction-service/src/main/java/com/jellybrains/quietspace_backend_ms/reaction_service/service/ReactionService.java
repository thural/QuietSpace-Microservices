package com.jellybrains.quietspace_backend_ms.reaction_service.service;

import com.jellybrains.quietspace_backend_ms.reaction_service.model.request.ReactionRequest;
import com.jellybrains.quietspace_backend_ms.reaction_service.model.response.ReactionResponse;
import com.jellybrains.quietspace_backend_ms.reaction_service.utils.enums.ContentType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReactionService {

    void handleReaction(ReactionRequest reaction);

    Optional<ReactionResponse> getUserReactionByContentId(UUID contentId);

    List<ReactionResponse> getLikesByContentId(UUID contentId);

    List<ReactionResponse> getDislikesByContentId(UUID contentId);

    Integer getLikeCountByContentId(UUID contentId);

    Integer getDislikeCountByContentId(UUID contentId);

    List<ReactionResponse> getReactionsByContentId(UUID contentId, ContentType type);

    List<ReactionResponse> getReactionsByUserId(UUID userId, ContentType contentType);

}
