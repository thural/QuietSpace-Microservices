package com.jellybrains.quietspace_backend_ms.userservice.common.client;

import com.jellybrains.quietspace_backend_ms.userservice.common.model.response.CommentResponse;

import java.util.Optional;

public interface CommentClient {
    Optional<CommentResponse> getCommentById(String commentId);
}
