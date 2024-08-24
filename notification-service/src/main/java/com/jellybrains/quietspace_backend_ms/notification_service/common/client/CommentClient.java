package com.jellybrains.quietspace_backend_ms.notification_service.common.client;

import com.jellybrains.quietspace_backend_ms.notification_service.common.model.response.CommentResponse;

import java.util.Optional;

public interface CommentClient {
    Optional<CommentResponse> getCommentById(String commentId);
}
