package com.jellybrains.quietspace.feed_service.repository;

import com.jellybrains.quietspace.feed_service.entity.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface PostRepository extends R2dbcRepository<Post, String> {

    Flux<Post> findAll(PageRequest pageRequest);

    Flux<Post> findAllByUserId(String userId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:query% OR p.text LIKE %:query%")
    Flux<Post> findAllByQuery(String query, PageRequest pageRequest);

    Mono<Void> deletePostsByUserId(String userId);

}