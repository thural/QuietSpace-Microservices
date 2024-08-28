package com.jellybrains.quietspace.chat_service.webclient.client.impl;

import com.jellybrains.quietspace.common_service.model.response.PostResponse;
import com.jellybrains.quietspace.chat_service.webclient.client.PostClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostClientImpl implements PostClient {

    private final WebClient webClient;
    private final String POST_API_URI = "/api/v1/posts/";

    @Override
    public Optional<PostResponse> getPostById(String postId) {
        return webClient.get()
                .uri(POST_API_URI + postId)
                .retrieve()
                .bodyToMono(PostResponse.class)
                .blockOptional();
    }

}
