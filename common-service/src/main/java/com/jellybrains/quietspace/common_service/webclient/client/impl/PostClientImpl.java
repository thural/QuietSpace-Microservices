package com.jellybrains.quietspace.common_service.webclient.client.impl;

import com.jellybrains.quietspace.common_service.model.response.PostResponse;
import com.jellybrains.quietspace.common_service.webclient.client.PostClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class PostClientImpl implements PostClient {

    private final WebClient webClient;
    private final String POST_API_URI = "/api/v1/posts/";

    @Override
    @CircuitBreaker(name = "feed-service",
            fallbackMethod = "com.jellybrains.quietspace.common_service.service.shared.FallbackService#genericFallback")
    public CompletableFuture<Optional<PostResponse>> getPostById(String postId) {
        return webClient.get()
                .uri(POST_API_URI + postId)
                .retrieve()
                .bodyToMono(PostResponse.class)
                .map(Optional::ofNullable)
                .toFuture();
    }

}
