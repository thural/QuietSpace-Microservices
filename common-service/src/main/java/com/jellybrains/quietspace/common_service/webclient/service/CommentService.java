package com.jellybrains.quietspace.common_service.webclient.service;

import com.jellybrains.quietspace.common_service.exception.CustomNotFoundException;
import com.jellybrains.quietspace.common_service.model.response.CommentResponse;
import com.jellybrains.quietspace.common_service.webclient.client.CommentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentService {

    private final CommentClient commentClient;

    public CommentResponse getCommentById(String commentId) {
        return commentClient.getCommentById(commentId)
                .orElseThrow(CustomNotFoundException::new);
    }

    public String getUserIdByCommentId(String postId) {
        return getCommentById(postId).getUserId(); // TODO: use kafka instead
    }
}
