package com.jellybrains.quietspace.feed_service.controller;

import com.jellybrains.quietspace.common_service.enums.NotificationType;
import com.jellybrains.quietspace.common_service.model.request.CommentRequest;
import com.jellybrains.quietspace.common_service.model.response.CommentResponse;
import com.jellybrains.quietspace.common_service.webclient.service.NotificationService;
import com.jellybrains.quietspace.feed_service.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    public static final String COMMENT_PATH = "/api/v1/comments";
    public static final String COMMENT_PATH_ID = "/{commentId}";

    private final CommentService commentService;
    private final NotificationService notificationService;


    @GetMapping("/post/{postId}")
    Page<CommentResponse> getCommentsByPostId(
            @PathVariable String postId,
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return commentService.getCommentsByPostId(postId, pageNumber, pageSize);
    }


    @GetMapping("/user")
    Page<CommentResponse> getCommentsByUserId(
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return commentService.getCommentsByUser(pageNumber, pageSize);
    }


    @GetMapping(COMMENT_PATH_ID + "/replies")
    Page<CommentResponse> getCommentRepliesById(
            @PathVariable String commentId,
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return commentService.getRepliesByParentId(commentId, pageNumber, pageSize);
    }


    @GetMapping(COMMENT_PATH_ID)
    ResponseEntity<CommentResponse> getCommentById(@PathVariable String commentId) {
        return commentService.getCommentById(commentId)
                .map(comment -> ResponseEntity.ok().body(comment))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    ResponseEntity<CommentResponse> createComment(@RequestBody @Validated CommentRequest comment) {
        CommentResponse response = commentService.createComment(comment);
        notificationService.processNotification(NotificationType.COMMENT, comment.getPostId());
        return ResponseEntity.ok(response);
    }


    @PatchMapping
    ResponseEntity<CommentResponse> patchComment(@RequestBody CommentRequest comment) {
        // TODO: broadcast the update over socket
        return ResponseEntity.ok(commentService.patchComment(comment));
    }

    @DeleteMapping(COMMENT_PATH_ID)
    ResponseEntity<?> deleteComment(@PathVariable String commentId) {
        // TODO: broadcast the update over socket
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

}