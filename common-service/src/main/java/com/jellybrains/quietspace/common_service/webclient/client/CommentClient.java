package com.jellybrains.quietspace.common_service.webclient.client;

import com.jellybrains.quietspace.common_service.model.response.CommentResponse;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface CommentClient {
    CompletableFuture<Optional<CommentResponse>> getCommentById(String commentId);
}
