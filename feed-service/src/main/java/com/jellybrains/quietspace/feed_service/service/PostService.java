package com.jellybrains.quietspace.feed_service.service;

import com.jellybrains.quietspace.common_service.model.request.PostRequest;
import com.jellybrains.quietspace.common_service.model.request.VoteRequest;
import com.jellybrains.quietspace.common_service.model.response.PostResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PostService {

    Page<PostResponse> getAllPosts(Integer pageNumber, Integer pageSize);

    PostResponse addPost(PostRequest post);

    Optional<PostResponse> getPostById(String id);

    void deletePost(String id);

    PostResponse patchPost(PostRequest post);

    void votePoll(VoteRequest voteRequest);

    Page<PostResponse> getPostsByUserId(String userId, Integer pageNumber, Integer pageSize);

    Page<PostResponse> getAllByQuery(String query, Integer pageNumber, Integer pageSize);

}
