package com.jellybrains.quietspace.feed_service.controller;

import com.jellybrains.quietspace.common_service.model.request.CommentRequest;
import com.jellybrains.quietspace.common_service.model.response.CommentResponse;
import com.jellybrains.quietspace.feed_service.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    public static final String COMMENT_PATH = "/api/v1/comments";
    public static final String COMMENT_PATH_ID = "/{commentId}";

    private final CommentService commentService;


    @GetMapping("/post/{postId}")
    Flux<CommentResponse> getCommentsByPostId(
            @PathVariable String postId,
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return commentService.getCommentsByPostId(postId, pageNumber, pageSize);
    }


    @GetMapping("/user")
    Flux<CommentResponse> getCommentsByUserId(
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return commentService.getCommentsByUser(pageNumber, pageSize);
    }


    @GetMapping(COMMENT_PATH_ID + "/replies")
    Flux<CommentResponse> getCommentRepliesById(
            @PathVariable String commentId,
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return commentService.getRepliesByParentId(commentId, pageNumber, pageSize);
    }


    @GetMapping(COMMENT_PATH_ID)
    Mono<ResponseEntity<CommentResponse>> getCommentById(@PathVariable String commentId) {
        return commentService.getCommentById(commentId)
                .map(comment -> ResponseEntity.ok().body(comment));
    }


    @PostMapping
    Mono<ResponseEntity<CommentResponse>> createComment(@RequestBody @Validated CommentRequest comment) {
        return commentService.createComment(comment)
                .map(response -> ResponseEntity.ok().body(response))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }


    @PatchMapping
    Mono<ResponseEntity<CommentResponse>> patchComment(@RequestBody CommentRequest comment) {
        // TODO: broadcast the update over socket
        return commentService.patchComment(comment)
                .map(response -> ResponseEntity.ok().body(response));
    }

    @DeleteMapping(COMMENT_PATH_ID)
    Mono<ResponseEntity<Void>> deleteComment(@PathVariable String commentId) {
        // TODO: broadcast the update over socket
        return commentService.deleteComment(commentId)
                .map(ResponseEntity::ok);
    }

}