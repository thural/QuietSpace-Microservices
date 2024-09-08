package com.jellybrains.quietspace.feed_service.repository;

import com.jellybrains.quietspace.feed_service.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PostRepository extends JpaRepository<Post, String> {

    Page<Post> findAllByUserId(String userId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:query% OR p.text LIKE %:query%")
    Page<Post> findAllByQuery(String query, PageRequest pageRequest);

}