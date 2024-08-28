package com.jellybrains.quietspace.notification_service.webclient.client;

import com.jellybrains.quietspace.common_service.model.response.CommentResponse;

import java.util.Optional;

public interface CommentClient {
    Optional<CommentResponse> getCommentById(String commentId);
}
