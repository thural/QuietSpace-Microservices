package com.jellybrains.quietspace_backend_ms.feedservice.repository;

import com.jellybrains.quietspace_backend_ms.feedservice.model.Comment;
import com.jellybrains.quietspace_backend_ms.feedservice.model.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentLikeRepository extends JpaRepository<CommentLike, UUID> {

    boolean existsByUserIdAndComment(UUID userId, Comment comment);

    List<CommentLike> getAllByUserId(UUID userId);

    List<CommentLike> getAllByComment(Comment comment);

    boolean existsByCommentIdAndUserId(UUID likeCommentId, UUID likeUserId);

    List<CommentLike> findAllByCommentId(UUID commentId);

    List<CommentLike> findAllByUserId(UUID userId);

    CommentLike findByCommentIdAndUserId(UUID likeCommentId, UUID likeUserId);
}