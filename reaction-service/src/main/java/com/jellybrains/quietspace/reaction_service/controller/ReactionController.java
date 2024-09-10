package com.jellybrains.quietspace.reaction_service.controller;

import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.ReactionType;
import com.jellybrains.quietspace.common_service.webclient.client.NotificationClient;
import com.jellybrains.quietspace.reaction_service.model.Reaction;
import com.jellybrains.quietspace.reaction_service.service.ReactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/reactions")
@RequiredArgsConstructor
public class ReactionController {
    private final ReactionService reactionService;
    private final NotificationClient notificationClient;

    @GetMapping("/hello")
    String sayHello() {
        return "hello from reaction service";
    }

    @GetMapping("/user")
    Page<Reaction> getReactionsByUser(
            @RequestParam String userId,
            @RequestParam ContentType contentType,
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        log.info("get reactions by user controller received a request");
        return reactionService.getReactionsByUserIdAndContentType(userId, contentType, pageNumber, pageSize);
    }

    @GetMapping("/content")
    Page<Reaction> getReactionsByContent(
            @RequestParam String contentId,
            @RequestParam ContentType contentType,
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        log.info("get reactions by content controller received a request");
        return reactionService.getReactionsByContentIdAndContentType(contentId, contentType, pageNumber, pageSize);
    }

    @PostMapping("/toggle-reaction")
    ResponseEntity<?> toggleReaction(@RequestBody Reaction reaction) {
        log.info("reactions toggle controller received a request");
        reactionService.handleReaction(reaction);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    ResponseEntity<Integer> countByContentIdAndReactionType(@RequestParam String contentId, @RequestParam ReactionType type) {
        log.info("reactions count controller received a request");
        return ResponseEntity.ok(reactionService.countByContentIdAndType(contentId, type));
    }

}
