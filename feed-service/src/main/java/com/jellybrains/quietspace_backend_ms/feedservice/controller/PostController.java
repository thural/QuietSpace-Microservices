package com.jellybrains.quietspace_backend_ms.feedservice.controller;

import com.jellybrains.quietspace_backend_ms.feedservice.model.request.PostRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.model.request.VoteRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.model.response.PostResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/api/v1/posts")
public class PostController {

    public static final String POST_PATH_ID = "/{postId}";

    private final PostService postService;

    @GetMapping
    Page<PostResponse> getAllPosts(@RequestParam(name = "page-number", required = false) Integer pageNumber,
                                   @RequestParam(name = "page-size", required = false) Integer pageSize) {
        return postService.getAllPosts(pageNumber, pageSize);
    }

    @GetMapping("/search")
    Page<PostResponse> getPostsByQuery(@RequestParam(name = "query") String query,
                                       @RequestParam(name = "page-number", required = false) Integer pageNumber,
                                       @RequestParam(name = "page-size", required = false) Integer pageSize) {
        return postService.getAllByQuery(query, pageNumber, pageSize);
    }

    @PostMapping
    ResponseEntity<?> createPost(@RequestBody @Validated PostRequest post) {
        PostResponse createdPost = postService.addPost(post);
        return new ResponseEntity<>(createdPost,HttpStatus.CREATED);
    }

    @GetMapping(POST_PATH_ID)
    ResponseEntity<?> getPostById(@PathVariable UUID postId) {
        Optional<PostResponse> optionalPost = postService.getPostById(postId);
        return new ResponseEntity<>(optionalPost.orElse(null), HttpStatus.OK);
    }

    @PutMapping(POST_PATH_ID)
    ResponseEntity<?> putPost(@PathVariable UUID postId,
                              @RequestBody @Validated PostRequest post) {
        PostResponse updatedPost = postService.updatePost(postId, post);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @DeleteMapping(POST_PATH_ID)
    ResponseEntity<?> deletePost(@PathVariable UUID postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(POST_PATH_ID)
    ResponseEntity<?> patchPost(@PathVariable UUID postId,
                                @RequestBody @Validated PostRequest post) {
        postService.patchPost(postId, post);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/vote-poll")
    ResponseEntity<?> votePoll(@RequestBody VoteRequest voteRequest) {
        postService.votePoll(voteRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}