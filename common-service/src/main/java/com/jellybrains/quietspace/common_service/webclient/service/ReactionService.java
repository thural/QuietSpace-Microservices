package com.jellybrains.quietspace.common_service.webclient.service;

import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.ReactionType;
import com.jellybrains.quietspace.common_service.model.response.ReactionResponse;
import com.jellybrains.quietspace.common_service.webclient.client.ReactionClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionClient reactionClient;


    public Integer getLikeCount(String contentId) {
        return reactionClient.countByContentIdAndReactionType(contentId, ReactionType.LIKE)
                .orElse(-1);
    }

    public Integer getDislikeCount(String contentId) {
        return reactionClient.countByContentIdAndReactionType(contentId, ReactionType.DISLIKE)
                .orElse(-1);
    }

    public ReactionResponse getUserReactionByContentId(String contentId, ContentType type) {
        return reactionClient.getUserReactionByContentId(contentId, type)
                .orElseGet(ReactionResponse::new);
    }
}
