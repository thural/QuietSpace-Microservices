package com.jellybrains.quietspace_backend_ms.reaction_service.common.service;

import com.jellybrains.quietspace_backend_ms.reaction_service.common.client.CommentClient;
import com.jellybrains.quietspace_backend_ms.reaction_service.common.exception.CustomNotFoundException;
import com.jellybrains.quietspace_backend_ms.reaction_service.common.model.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentService {

    private final CommentClient commentClient;

    public CommentResponse getCommentById(String commentId){
        return commentClient.getCommentById(commentId)
                .orElseThrow(CustomNotFoundException::new);
    }

    public String getUserIdByCommentId(String postId){
        return getCommentById(postId).getUserId(); // TODO: use kafka instead
    }
}
