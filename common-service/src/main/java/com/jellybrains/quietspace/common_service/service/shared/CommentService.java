package com.jellybrains.quietspace.common_service.service.shared;

import com.jellybrains.quietspace.common_service.model.response.CommentResponse;
import com.jellybrains.quietspace.common_service.webclient.client.CommentClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class CommentService {

    private final CommentClient commentClient;


    @TimeLimiter(name = "feed-service")
    @CircuitBreaker(name = "feed-service")
    public CompletableFuture<Optional<CommentResponse>> getCommentById(String commentId) {
        return commentClient.getCommentById(commentId);
    }

    @TimeLimiter(name = "feed-service")
    @CircuitBreaker(name = "feed-service")
    public CompletableFuture<String> getUserIdByCommentId(String postId) {
        return getCommentById(postId)
                .thenApply(optional -> optional.map(CommentResponse::getUserId).orElseThrow());
    }
}
