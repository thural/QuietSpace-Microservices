package com.jellybrains.quietspace.feed_service.controller;

import com.jellybrains.quietspace.common_service.model.request.PostRequest;
import com.jellybrains.quietspace.common_service.model.request.VoteRequest;
import com.jellybrains.quietspace.common_service.model.response.PostResponse;
import com.jellybrains.quietspace.feed_service.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;


    @GetMapping
    ResponseEntity<Flux<PostResponse>> getAllPosts(
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize) {
        return ResponseEntity.ok(postService.getAllPosts(pageNumber, pageSize));
    }


    @GetMapping("/user/{userId}")
    ResponseEntity<Flux<PostResponse>> getPostsByUserId(
            @PathVariable String userId,
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return ResponseEntity.ok(postService.getPostsByUserId(userId, pageNumber, pageSize));
    }


    @GetMapping("/search")
    ResponseEntity<Flux<PostResponse>> getPostsByQuery(
            @RequestParam(name = "query", required = true) String query,
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return ResponseEntity.ok(postService.getAllByQuery(query, pageNumber, pageSize));
    }


    @GetMapping("/{postId}")
    Mono<ResponseEntity<PostResponse>> getPostById(@PathVariable String postId) {
        return postService.getPostById(postId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }


    @PostMapping
    Mono<ResponseEntity<PostResponse>> createPost(@RequestBody @Validated PostRequest post) {
        return postService.addPost(post).map(ResponseEntity::ok);
    }


    @PatchMapping
    Mono<ResponseEntity<PostResponse>> patchPost(@RequestBody @Validated PostRequest post) {
        return postService.patchPost(post).map(ResponseEntity::ok);
    }


    @DeleteMapping("/{postId}")
    ResponseEntity<Void> deletePost(@PathVariable String postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/vote-poll")
    ResponseEntity<Void> votePoll(@RequestBody VoteRequest voteRequest) {
        postService.votePoll(voteRequest);
        return ResponseEntity.noContent().build();
    }


}