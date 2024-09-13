package com.jellybrains.quietspace.feed_service.repository;

import com.jellybrains.quietspace.feed_service.entity.Comment;
import com.jellybrains.quietspace.feed_service.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface CommentRepository extends ReactiveCrudRepository<Comment, String> {

    Flux<Comment> findAllByPostId(String postId, Pageable pageable);

    Mono<Integer> countByParentIdAndPost(String parentId, Post post);

    Mono<Void> deleteAllByParentId(String parentId);

    Flux<Comment> findAllByParentId(String commentId, Pageable pageable);

    Flux<Comment> findAllByUserId(String userId, Pageable pageable);

    Mono<Void> deleteCommentsByUserId(String userId);

}
