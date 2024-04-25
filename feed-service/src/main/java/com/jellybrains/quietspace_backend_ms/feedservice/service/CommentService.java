package com.jellybrains.quietspace_backend_ms.feedservice.service;

import com.jellybrains.quietspace_backend_ms.feedservice.dto.request.CommentRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.CommentLikeResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.CommentResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentService {
    Page<CommentResponse> getCommentsByPost(UUID postId, Integer pageNumber, Integer pageSize);

    CommentResponse createComment(CommentRequest comment);

    Optional<CommentResponse> getCommentById(UUID id);

    void updateComment(UUID commentId, CommentRequest comment);

    void deleteComment(UUID id);

    void patchComment(UUID id, CommentRequest comment);

    void toggleCommentLike(UUID commentId);

    List<CommentLikeResponse> getLikesByCommentId(UUID commentId);

    List<CommentLikeResponse> getAllByUserId(UUID userId);
}
