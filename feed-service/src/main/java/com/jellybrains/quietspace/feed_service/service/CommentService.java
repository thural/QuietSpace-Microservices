package com.jellybrains.quietspace.feed_service.service;

import com.jellybrains.quietspace.common_service.model.request.CommentRequest;
import com.jellybrains.quietspace.common_service.model.response.CommentResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CommentService {

    Page<CommentResponse> getCommentsByPostId(String postId, Integer pageNumber, Integer pageSize);

    CommentResponse createComment(CommentRequest comment);

    Optional<CommentResponse> getCommentById(String id);

    void deleteComment(String id);

    Page<CommentResponse> getRepliesByParentId(String commentId, Integer pageNumber, Integer pageSize);

    CommentResponse patchComment(CommentRequest comment);

    Page<CommentResponse> getCommentsByUser(Integer pageNumber, Integer pageSize);
    
}
