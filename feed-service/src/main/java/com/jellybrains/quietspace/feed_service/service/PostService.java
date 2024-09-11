package com.jellybrains.quietspace.feed_service.service;

import com.jellybrains.quietspace.common_service.model.request.PostRequest;
import com.jellybrains.quietspace.common_service.model.request.VoteRequest;
import com.jellybrains.quietspace.common_service.model.response.PostResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostService {

    Flux<PostResponse> getAllPosts(Integer pageNumber, Integer pageSize);

    Mono<PostResponse> addPost(PostRequest post);

    Mono<PostResponse> getPostById(String id);

    Mono<Void> deletePost(String id);

    Mono<PostResponse> patchPost(PostRequest post);

    Mono<Void> votePoll(VoteRequest voteRequest);

    Flux<PostResponse> getPostsByUserId(String userId, Integer pageNumber, Integer pageSize);

    Flux<PostResponse> getAllByQuery(String query, Integer pageNumber, Integer pageSize);

}
