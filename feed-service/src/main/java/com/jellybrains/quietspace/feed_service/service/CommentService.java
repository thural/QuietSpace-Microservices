package com.jellybrains.quietspace.feed_service.service;

import com.jellybrains.quietspace.common_service.model.request.CommentRequest;
import com.jellybrains.quietspace.common_service.model.response.CommentResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentService {

    Flux<CommentResponse> getCommentsByPostId(String postId, Integer pageNumber, Integer pageSize);

    Mono<CommentResponse> createComment(CommentRequest comment);

    Mono<CommentResponse> getCommentById(String id);

    Mono<Void> deleteComment(String id);

    Flux<CommentResponse> getRepliesByParentId(String commentId, Integer pageNumber, Integer pageSize);

    Mono<CommentResponse> patchComment(CommentRequest comment);

    Flux<CommentResponse> getCommentsByUser(Integer pageNumber, Integer pageSize);

}
