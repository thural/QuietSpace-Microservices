package com.jellybrains.quietspace.common_service.service.shared;

import com.jellybrains.quietspace.common_service.model.response.CommentResponse;
import com.jellybrains.quietspace.common_service.webclient.client.CommentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class CommentService {

    private final CommentClient commentClient;

    public CompletableFuture<Optional<CommentResponse>> getCommentById(String commentId) {
        return commentClient.getCommentById(commentId);
    }

    public CompletableFuture<String> getUserIdByCommentId(String postId) {
        return getCommentById(postId)
                .thenApply(optional -> optional.map(CommentResponse::getUserId).orElseThrow());
    }
}
