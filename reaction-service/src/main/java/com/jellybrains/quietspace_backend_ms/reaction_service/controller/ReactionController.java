package com.jellybrains.quietspace_backend_ms.reaction_service.controller;

import com.jellybrains.quietspace_backend_ms.reaction_service.model.response.ReactionResponse;
import com.jellybrains.quietspace_backend_ms.reaction_service.service.ReactionService;
import com.jellybrains.quietspace_backend_ms.reaction_service.utils.enums.ContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/api/v1/reactions")
public class ReactionController {

    private final ReactionService reactionService;

    @GetMapping("/post/users/{userId}")
    Page<ReactionResponse> getPostReactionsByUser(@PathVariable("userId") String userId,
                                                  @RequestParam(required = false) Integer pageNumber,
                                                  @RequestParam(required = false) Integer pageSize) {
        return reactionService.getReactionsByUserId(userId, ContentType.POST, pageNumber, pageSize);
    }

    @GetMapping("/comment/users/{userId}")
    Page<ReactionResponse> getCommentReactionsByUser(@PathVariable("userId") String userId,
                                                     @RequestParam(required = false) Integer pageNumber,
                                                     @RequestParam(required = false) Integer pageSize) {
        return reactionService.getReactionsByUserId(userId, ContentType.COMMENT, pageNumber, pageSize);
    }

    @GetMapping("/post/{postId}")
    Page<ReactionResponse> getPostReactions(@PathVariable("postId") String postId,
                                            @RequestParam(required = false) Integer pageNumber,
                                            @RequestParam(required = false) Integer pageSize) {
        return reactionService.getReactionsByUserId(postId, ContentType.POST, pageNumber, pageSize);
    }

    @GetMapping("/comment/{commentId}")
    Page<ReactionResponse> getCommentReactions(@PathVariable("commentId") String commentId,
                                               @RequestParam(required = false) Integer pageNumber,
                                               @RequestParam(required = false) Integer pageSize) {
        return reactionService.getReactionsByUserId(commentId, ContentType.POST, pageNumber, pageSize);
    }

    @GetMapping("/user/{userId}")
    Page<ReactionResponse> getAllReactionsByUser(@PathVariable("userId") String userId,
                                                 @RequestParam(required = false) Integer pageNumber,
                                                 @RequestParam(required = false) Integer pageSize) {
        return reactionService.getAllReactionsByUserId(userId, ContentType.POST, pageNumber, pageSize);
    }

    @GetMapping("/content/{contentId}")
    ResponseEntity<ReactionResponse> getReactionById(@PathVariable("contentId") String contentId){
        return reactionService.getUserReactionByContentId(contentId)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("content/{contentId}/like-count")
    ResponseEntity<Integer> getLikeCountByContentId(@PathVariable("contentId") String contentId){
        return ResponseEntity.ok(reactionService.getLikeCountByContentId(contentId));
    }

    @GetMapping("content/{contentId}/dislike-count")
    ResponseEntity<Integer> getdislikeeCountByContentId(@PathVariable("contentId") String contentId){
        return ResponseEntity.ok(reactionService.getDislikeCountByContentId(contentId));
    }

}
