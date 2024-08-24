package com.jellybrains.quietspace_backend_ms.reaction_service.common.service;

import com.jellybrains.quietspace_backend_ms.reaction_service.common.client.ReactionClient;
import com.jellybrains.quietspace_backend_ms.reaction_service.common.exception.CustomNotFoundException;
import com.jellybrains.quietspace_backend_ms.reaction_service.common.model.response.ReactionResponse;
import com.jellybrains.quietspace_backend_ms.reaction_service.common.enums.ReactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionClient reactionClient;


    public Integer getLikeCount(String contentId) {
        return reactionClient.countByContentIdAndReactionType(contentId, ReactionType.LIKE)
                .orElseThrow(CustomNotFoundException::new);
    }

    public Integer getDislikeCount(String contentId) {
        return reactionClient.countByContentIdAndReactionType(contentId, ReactionType.LIKE)
                .orElseThrow(CustomNotFoundException::new);
    }

    public ReactionResponse getUserReactionByContentId(String contentId){
        return reactionClient.getUserReactionByContentId(contentId)
                .orElseThrow(CustomNotFoundException::new);
    }
}
