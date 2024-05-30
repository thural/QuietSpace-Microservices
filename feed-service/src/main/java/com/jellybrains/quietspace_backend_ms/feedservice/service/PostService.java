package com.jellybrains.quietspace_backend_ms.feedservice.service;

import com.jellybrains.quietspace_backend_ms.feedservice.entity.Poll;
import com.jellybrains.quietspace_backend_ms.feedservice.model.request.PostRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.model.request.VoteRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.model.response.PostResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface PostService {

    Page<PostResponse> getAllPosts(Integer pageNumber, Integer pageSize);

    PostResponse addPost(PostRequest post);

    Optional<PostResponse> getPostById(UUID id);

    PostResponse updatePost(UUID id, PostRequest post);

    void deletePost(UUID id);

    PostResponse patchPost(UUID id, PostRequest post);

    void votePoll(VoteRequest voteRequest);

    String getVotedPollOptionLabel(Poll poll);

    Page<PostResponse> getPostsByUserId(UUID userId, Integer pageNumber, Integer pageSize);

    Page<PostResponse> getAllByQuery(String query, Integer pageNumber, Integer pageSize);
}
