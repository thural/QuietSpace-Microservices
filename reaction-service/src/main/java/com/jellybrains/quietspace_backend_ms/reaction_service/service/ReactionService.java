package com.jellybrains.quietspace_backend_ms.reaction_service.service;

import com.jellybrains.quietspace_backend_ms.reaction_service.model.request.ReactionRequest;
import com.jellybrains.quietspace_backend_ms.reaction_service.model.response.ReactionResponse;
import com.jellybrains.quietspace_backend_ms.reaction_service.utils.enums.ContentType;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ReactionService {

    void handleReaction(ReactionRequest reaction);

    Optional<ReactionResponse> getUserReactionByContentId(String contentId);

    Page<ReactionResponse> getLikesByContentId(String contentId, Integer pageNumber, Integer pageSize);

    Page<ReactionResponse> getDislikesByContentId(String contentId, Integer pageNumber, Integer pageSize);

    Integer getLikeCountByContentId(String contentId);

    Integer getDislikeCountByContentId(String contentId);

    Page<ReactionResponse> getReactionsByContentId(String contentId, ContentType type, Integer pageNumber, Integer pageSize);

    Page<ReactionResponse> getReactionsByUserId(String userId, ContentType contentType, Integer pageNumber, Integer pageSize);

    Page<ReactionResponse> getAllReactionsByUserId(String userId, ContentType contentType, Integer pageNumber, Integer pageSize);
}
