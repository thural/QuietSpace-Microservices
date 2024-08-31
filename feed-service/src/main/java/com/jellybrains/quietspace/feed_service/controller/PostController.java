package com.jellybrains.quietspace.feed_service.controller;

import com.jellybrains.quietspace.common_service.model.request.PostRequest;
import com.jellybrains.quietspace.common_service.model.request.VoteRequest;
import com.jellybrains.quietspace.common_service.model.response.PostResponse;
import com.jellybrains.quietspace.feed_service.service.PostService;
import com.jellybrains.quietspace.feed_service.webclient.client.ReactionClient;
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

    public static final String POST_PATH = "/api/v1/feed";
    public static final String POST_PATH_ID = "/{postId}";

    private final PostService postService;

    private final ReactionClient reactionClient;

    @GetMapping("hello-reactions")
    public String helloFromReactionService(){
        return reactionClient.sayHello()
                .orElseGet(() -> "failed to receive hello from reaction service");
    }

    @GetMapping
    Page<PostResponse> getAllPosts(
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return postService.getAllPosts(pageNumber, pageSize);
    }

    @GetMapping("/search")
    Page<PostResponse> getPostsByQuery(
            @RequestParam(name = "query", required = true) String query,
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return postService.getAllByQuery(query, pageNumber, pageSize);
    }

    @PostMapping
    ResponseEntity<PostResponse> createPost(@RequestBody @Validated PostRequest post) {
        return ResponseEntity.ok(postService.addPost(post));
    }

    @GetMapping(POST_PATH_ID)
    ResponseEntity<PostResponse> getPostById(@PathVariable String postId) {
        return postService.getPostById(postId)
                .map(post -> ResponseEntity.ok().body(post))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(POST_PATH_ID)
    ResponseEntity<PostResponse> putPost(@PathVariable String postId, @RequestBody @Validated PostRequest post) {
        return ResponseEntity.ok(postService.updatePost(postId, post));
    }

    @DeleteMapping(POST_PATH_ID)
    ResponseEntity<Void> deletePost(@PathVariable String postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(POST_PATH_ID)
    ResponseEntity<PostResponse> patchPost(@PathVariable String postId, @RequestBody PostRequest post) {
        return ResponseEntity.ok(postService.patchPost(postId, post));
    }

    @PostMapping("/vote-poll")
    ResponseEntity<Void> votePoll(@RequestBody VoteRequest voteRequest) {
        postService.votePoll(voteRequest);
        return ResponseEntity.ok().build();
    }

}