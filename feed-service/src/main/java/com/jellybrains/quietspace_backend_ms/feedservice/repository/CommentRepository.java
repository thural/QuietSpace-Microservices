package com.jellybrains.quietspace_backend_ms.feedservice.repository;

import com.jellybrains.quietspace_backend_ms.feedservice.entity.Comment;
import com.jellybrains.quietspace_backend_ms.feedservice.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Page<Comment> findAllByPostId(UUID postId, Pageable pageable);

    Integer countByParentIdAndPost(UUID parentId, Post post);

    void deleteAllByParentId(UUID parentId);

    Page<Comment> findAllByParentId(UUID commentId, Pageable pageable);

    Page<Comment> findAllByUserId(UUID userId, PageRequest pageRequest);
}
