package com.jellybrains.quietspace_backend_ms.userservice.common.service;

import com.jellybrains.quietspace_backend_ms.userservice.common.client.ReactionClient;
import com.jellybrains.quietspace_backend_ms.userservice.common.enums.ReactionType;
import com.jellybrains.quietspace_backend_ms.userservice.common.exception.CustomNotFoundException;
import com.jellybrains.quietspace_backend_ms.userservice.common.model.response.ReactionResponse;
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
