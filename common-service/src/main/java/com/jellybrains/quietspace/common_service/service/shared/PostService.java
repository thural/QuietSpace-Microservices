package com.jellybrains.quietspace.common_service.service.shared;

import com.jellybrains.quietspace.common_service.model.response.PostResponse;
import com.jellybrains.quietspace.common_service.webclient.client.PostClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class PostService {

    private final PostClient postClient;


    @TimeLimiter(name = "feed-service")
    @CircuitBreaker(name = "feed-service")
    public CompletableFuture<Optional<PostResponse>> getPostById(String postId) {
        return postClient.getPostById(postId);
    }


    @TimeLimiter(name = "feed-service")
    @CircuitBreaker(name = "feed-service")
    public CompletableFuture<String> getUserIdByPostId(String postId) {
        return getPostById(postId)
                .thenApply(optional -> optional.map(PostResponse::getUserId).orElseThrow());
    }
}
