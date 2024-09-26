package com.jellybrains.quietspace.common_service.service.shared;

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
                .thenApply(optional -> optional.orElse(-1)).join();
    }

    public Integer getDislikeCount(String contentId) {
        return reactionClient.countByContentIdAndReactionType(contentId, ReactionType.DISLIKE)
                .thenApply(optional -> optional.orElse(-1)).join();
    }

    public ReactionResponse getUserReactionByContentId(String contentId, ContentType type) {
        return reactionClient.getUserReactionByContentId(contentId, type)
                .thenApply(optional -> optional.orElseGet(ReactionResponse::new)).join();
    }
}
