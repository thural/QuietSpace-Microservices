package com.jellybrains.quietspace_backend_ms.reaction_service.client;


import com.jellybrains.quietspace_backend_ms.reaction_service.model.response.ReactionResponse;

import java.util.UUID;

public interface ReactionClient {

    public Integer getLikeCountByContentId(UUID contentId);

    public Integer getDislikeCountByContentId(UUID contentId);

    public ReactionResponse getUserReactionByContentId(UUID contentId);

}
