package com.jellybrains.quietspace_backend_ms.feedservice.service;

import com.jellybrains.quietspace.common_service.model.request.PostRequest;
import com.jellybrains.quietspace.common_service.model.request.VoteRequest;
import com.jellybrains.quietspace.common_service.model.response.PostResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.entity.Poll;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PostService {

    Page<PostResponse> getAllPosts(Integer pageNumber, Integer pageSize);

    PostResponse addPost(PostRequest post);

    Optional<PostResponse> getPostById(String id);

    PostResponse updatePost(String id, PostRequest post);

    void deletePost(String id);

    PostResponse patchPost(String id, PostRequest post);

    void votePoll(VoteRequest voteRequest);

    String getVotedPollOptionLabel(Poll poll);

    Page<PostResponse> getPostsByUserId(String userId, Integer pageNumber, Integer pageSize);

    Page<PostResponse> getAllByQuery(String query, Integer pageNumber, Integer pageSize);
}
