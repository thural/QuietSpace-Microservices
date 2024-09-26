package com.jellybrains.quietspace.common_service.webclient.client.impl;

import com.jellybrains.quietspace.common_service.model.response.CommentResponse;
import com.jellybrains.quietspace.common_service.webclient.client.CommentClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentClientImpl implements CommentClient {

    private final WebClient webClient;
    private final String COMMENT_API_URI = "/api/v1/comments/";

    @Override
    @CircuitBreaker(name = "feed-service",
            fallbackMethod = "com.jellybrains.quietspace.common_service.service.shared.FallbackService#genericFallback")
    public Optional<CommentResponse> getCommentById(String commentId) {
        return webClient.get()
                .uri(COMMENT_API_URI + commentId)
                .retrieve()
                .bodyToMono(CommentResponse.class)
                .blockOptional();
    }

}
