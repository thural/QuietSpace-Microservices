package com.jellybrains.quietspace_backend_ms.feedservice.repository;

import com.jellybrains.quietspace_backend_ms.feedservice.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {

    List<PostLike> findAllByPostId(UUID postId);

    List<PostLike> findAllByUserId(UUID userId);

    List<PostLike> findAllByPostIdAndUserId(UUID postId, UUID userId);

    boolean existsByPostIdAndUserId(UUID likePostId, UUID likeUserId);
}