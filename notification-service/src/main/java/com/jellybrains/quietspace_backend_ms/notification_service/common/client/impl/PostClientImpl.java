package com.jellybrains.quietspace_backend_ms.notification_service.common.client.impl;

import com.jellybrains.quietspace_backend_ms.notification_service.common.client.PostClient;
import com.jellybrains.quietspace_backend_ms.notification_service.common.model.response.PostResponse;
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
