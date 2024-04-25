package com.jellybrains.quietspace_backend_ms.feedservice.repository;

import com.jellybrains.quietspace_backend_ms.feedservice.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Page<Comment> findAllByPostId(UUID postId, Pageable pageable);
}
