package com.jellybrains.quietspace_backend_ms.feedservice.service;

import com.jellybrains.quietspace_backend_ms.feedservice.dto.request.PostRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.PostLikeResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.PostResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostService {
    Page<PostResponse> getAllPosts(Integer pageNumber, Integer pageSize);

    PostResponse addPost(PostRequest post);

    Optional<PostResponse> getPostById(UUID id);

    void updatePost(UUID id, PostRequest post);

    void deletePost(UUID id);

    void patchPost(UUID id, PostRequest post);

    Page<PostResponse> getPostsByUserId(UUID userId, Integer pageNumber, Integer pageSize);

    List<PostLikeResponse> getPostLikesByPostId(UUID postId);

    void togglePostLike(UUID postId);

    List<PostLikeResponse> getPostLikesByUserId(UUID userId);
}
