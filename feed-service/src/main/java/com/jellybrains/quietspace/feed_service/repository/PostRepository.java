package com.jellybrains.quietspace.feed_service.repository;

import com.jellybrains.quietspace.feed_service.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface PostRepository extends ReactiveCrudRepository<Post, String> {

    @Query("SELECT p FROM Post p")
    Flux<Post> findAllPaged(Pageable pageable);


    Flux<Post> findAllByUserId(String userId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.text LIKE %:keyword%")
    Flux<Post> searchByTitleOrText(@Param("keyword") String keyword, Pageable pageable);

    Mono<Void> deletePostsByUserId(String userId);

}