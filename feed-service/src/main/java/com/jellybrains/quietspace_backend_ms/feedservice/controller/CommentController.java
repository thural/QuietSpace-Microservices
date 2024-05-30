package com.jellybrains.quietspace_backend_ms.feedservice.controller;

import com.jellybrains.quietspace_backend_ms.feedservice.model.request.CommentRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.model.response.CommentResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.service.CommentService;
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
@RequestMapping("/api/v1/comments")
public class CommentController {

    public static final String COMMENT_PATH_ID = "/{commentId}";

    private final CommentService commentService;


    @GetMapping("/post/{postId}")
    Page<CommentResponse> getCommentsByPostId(@PathVariable UUID postId,
                                              @RequestParam(required = false) Integer pageNumber,
                                              @RequestParam(required = false) Integer pageSize) {
        return commentService.getCommentsByPost(postId, pageNumber, pageSize);
    }

    @GetMapping("/user/{userId}")
    Page<CommentResponse> getCommentsByUserId(@PathVariable UUID userId,
                                              @RequestParam(required = false) Integer pageNumber,
                                              @RequestParam(required = false) Integer pageSize) {
        return commentService.getCommentsByUser(userId, pageNumber, pageSize);
    }

    @GetMapping(COMMENT_PATH_ID + "/replies")
    Page<CommentResponse> getCommentRepliesById(@PathVariable UUID commentId,
                                              @RequestParam(required = false) Integer pageNumber,
                                              @RequestParam(required = false) Integer pageSize) {
        return commentService.getRepliesByParentId(commentId, pageNumber, pageSize);
    }

    @GetMapping(COMMENT_PATH_ID)
    CommentResponse getCommentById(@PathVariable UUID commentId) {
        Optional<CommentResponse> optionalComment = commentService.getCommentById(commentId);
        return optionalComment.orElse(null);
    }

    @PostMapping
    ResponseEntity<?> createComment(@RequestBody @Validated CommentRequest comment) {
        CommentResponse createdComment = commentService.createComment(comment);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @PutMapping(COMMENT_PATH_ID)
    ResponseEntity<?> putComment(@PathVariable UUID commentId,
                                 @RequestBody @Validated CommentRequest comment) {
        CommentResponse patchedComment = commentService.updateComment(commentId, comment);
        return new ResponseEntity<>(patchedComment, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(COMMENT_PATH_ID)
    ResponseEntity<?> deleteComment(@PathVariable UUID commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(COMMENT_PATH_ID)
    ResponseEntity<?> patchComment(@PathVariable UUID commentId,
                                   @RequestBody CommentRequest comment) {
        CommentResponse patchedComment = commentService.patchComment(commentId, comment);
        return new ResponseEntity<>(patchedComment, HttpStatus.OK);
    }

}
