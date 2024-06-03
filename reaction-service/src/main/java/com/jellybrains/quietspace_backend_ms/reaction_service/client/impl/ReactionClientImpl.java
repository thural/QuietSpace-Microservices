package com.jellybrains.quietspace_backend_ms.reaction_service.client.impl;

import com.jellybrains.quietspace_backend_ms.reaction_service.client.ReactionClient;
import com.jellybrains.quietspace_backend_ms.reaction_service.model.response.ReactionResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReactionClientImpl implements ReactionClient {

    @Override
    public Integer getLikeCountByContentId(UUID contentId) {
        return 0; // TODO: use webclient
    }

    @Override
    public Integer getDislikeCountByContentId(UUID contentId) {
        return 0; // TODO: use webclient
    }

    @Override
    public ReactionResponse getUserReactionByContentId(UUID contentId) {
        return null; // TODO: use webclient
    }

}
