package com.jellybrains.quietspace_backend_ms.feedservice.service;

import com.jellybrains.quietspace_backend_ms.feedservice.model.request.CommentRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.model.response.CommentResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CommentService {

    Page<CommentResponse> getCommentsByPostId(String postId, Integer pageNumber, Integer pageSize);

    CommentResponse createComment(CommentRequest comment);

    Optional<CommentResponse> getCommentById(String id);

    CommentResponse updateComment(String commentId, CommentRequest comment);

    void deleteComment(String id);

    Page<CommentResponse> getRepliesByParentId(String commentId, Integer pageNumber, Integer pageSize);

    CommentResponse patchComment(String id, CommentRequest comment);

    Page<CommentResponse> getCommentsByUserId(String userId, Integer pageNumber, Integer pageSize);
}
