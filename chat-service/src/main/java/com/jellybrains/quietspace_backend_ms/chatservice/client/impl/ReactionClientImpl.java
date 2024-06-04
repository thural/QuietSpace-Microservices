package com.jellybrains.quietspace_backend_ms.chatservice.client.impl;

import com.jellybrains.quietspace_backend_ms.chatservice.client.ReactionClient;
import com.jellybrains.quietspace_backend_ms.chatservice.model.response.ReactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReactionClientImpl implements ReactionClient {

    private final WebClient webClient;
    private final String REACTION_API_URI = "/api/v1/reactions/";

    @Override
    public Integer getLikeCountByContentId(UUID contentId) {
        return webClient.get()
                .uri(REACTION_API_URI + "content/" + contentId + "/like-count")
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
    }

    @Override
    public Integer getDislikeCountByContentId(UUID contentId) {
        return webClient.get()
                .uri(REACTION_API_URI + "content/" + contentId + "/dislike-count")
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
    }

    @Override
    public Optional<ReactionResponse> getUserReactionByContentId(UUID contentId) {
        return webClient.get()
                .uri(REACTION_API_URI + "content/" + contentId)
                .retrieve()
                .bodyToMono(ReactionResponse.class)
                .blockOptional();
    }

}
