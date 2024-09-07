package com.jellybrains.quietspace.feed_service.webclient.service;

import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.ReactionType;
import com.jellybrains.quietspace.common_service.model.response.ReactionResponse;
import com.jellybrains.quietspace.feed_service.exception.CustomNotFoundException;
import com.jellybrains.quietspace.feed_service.webclient.client.ReactionClient;
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
        return reactionClient.countByContentIdAndReactionType(contentId, ReactionType.DISLIKE)
                .orElseThrow(CustomNotFoundException::new);
    }

    public ReactionResponse getUserReactionByContentId(String contentId, ContentType type) {
        return reactionClient.getUserReactionByContentId(contentId, type)
                .orElseThrow(CustomNotFoundException::new);
    }
}
