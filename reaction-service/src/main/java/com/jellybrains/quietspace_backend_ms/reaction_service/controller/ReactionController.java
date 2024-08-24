package com.jellybrains.quietspace_backend_ms.reaction_service.controller;

import com.jellybrains.quietspace_backend_ms.reaction_service.common.client.NotificationClient;
import com.jellybrains.quietspace_backend_ms.reaction_service.common.enums.ContentType;
import com.jellybrains.quietspace_backend_ms.reaction_service.common.enums.ReactionType;
import com.jellybrains.quietspace_backend_ms.reaction_service.model.Reaction;
import com.jellybrains.quietspace_backend_ms.reaction_service.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/reactions")
@RequiredArgsConstructor
public class ReactionController {
    private final ReactionService reactionService;
    private final NotificationClient notificationClient;

    @GetMapping("/user")
    Page<Reaction> getReactionsByUser(
            @RequestParam String userId,
            @RequestParam ContentType contentType,
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return reactionService.getReactionsByUserIdAndContentType(userId, contentType, pageNumber, pageSize);
    }

    @GetMapping("/content")
    Page<Reaction> getReactionsByContent(
            @RequestParam String contentId,
            @RequestParam ContentType contentType,
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return reactionService.getReactionsByContentIdAndContentType(contentId, contentType, pageNumber, pageSize);
    }

    @PostMapping("/toggle-reaction")
    ResponseEntity<?> toggleReaction(@RequestBody Reaction reaction) {
        reactionService.handleReaction(reaction);
        notificationClient.processNotificationByReaction(reaction.getContentType(), reaction.getContentId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    ResponseEntity<Integer> countByContentIdAndReactionType(@RequestParam String contentId, @RequestParam ReactionType type) {
        return ResponseEntity.ok(reactionService.countByContentIdAndReactionType(contentId, type));
    }

}
