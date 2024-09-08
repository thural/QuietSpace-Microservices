package com.jellybrains.quietspace.feed_service.controller;

import com.jellybrains.quietspace.common_service.model.request.PostRequest;
import com.jellybrains.quietspace.common_service.model.request.VoteRequest;
import com.jellybrains.quietspace.common_service.model.response.PostResponse;
import com.jellybrains.quietspace.feed_service.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;


    @GetMapping
    Page<PostResponse> getAllPosts(
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return postService.getAllPosts(pageNumber, pageSize);
    }


    @GetMapping("/user/{userId}")
    Page<PostResponse> getPostsByUserId(
            @PathVariable String userId,
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return postService.getPostsByUserId(userId, pageNumber, pageSize);
    }


    @GetMapping("/search")
    Page<PostResponse> getPostsByQuery(
            @RequestParam(name = "query", required = true) String query,
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return postService.getAllByQuery(query, pageNumber, pageSize);
    }


    @GetMapping("/{postId}")
    ResponseEntity<PostResponse> getPostById(@PathVariable String postId) {
        return postService.getPostById(postId)
                .map(post -> ResponseEntity.ok().body(post))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    ResponseEntity<PostResponse> createPost(@RequestBody @Validated PostRequest post) {
        return ResponseEntity.ok(postService.addPost(post));
    }


    @PatchMapping
    ResponseEntity<PostResponse> patchPost(@RequestBody @Validated PostRequest post) {
        return ResponseEntity.ok(postService.patchPost(post));
    }


    @DeleteMapping("/{postId}")
    ResponseEntity<Void> deletePost(@PathVariable String postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/vote-poll")
    ResponseEntity<Void> votePoll(@RequestBody VoteRequest voteRequest) {
        postService.votePoll(voteRequest);
        return ResponseEntity.ok().build();
    }


}