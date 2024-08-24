package com.jellybrains.quietspace_backend_ms.feedservice.repository;

import com.jellybrains.quietspace_backend_ms.feedservice.entity.Comment;
import com.jellybrains.quietspace_backend_ms.feedservice.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, String> {
    Page<Comment> findAllByPostId(String postId, Pageable pageable);

    Integer countByParentIdAndPost(String parentId, Post post);

    void deleteAllByParentId(String parentId);

    Page<Comment> findAllByParentId(String commentId, Pageable pageable);

    Page<Comment> findAllByUserId(String userId, PageRequest pageRequest);
}
