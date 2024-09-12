package com.jellybrains.quietspace.reaction_service.controller;

import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.ReactionType;
import com.jellybrains.quietspace.reaction_service.model.Reaction;
import com.jellybrains.quietspace.reaction_service.service.ReactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService service;


    @GetMapping("/user")
    ResponseEntity<Flux<Reaction>> getReactionsByUser(
            @RequestParam String userId,
            @RequestParam ContentType contentType,
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return ResponseEntity.ok(service.getReactionsByUserIdAndContentType(userId, contentType, pageNumber, pageSize));
    }


    @GetMapping("/content")
    ResponseEntity<Flux<Reaction>> getReactionsByContent(
            @RequestParam String contentId,
            @RequestParam ContentType contentType,
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return ResponseEntity.ok(service.getAllByContentIdAndContentType(contentId, contentType, pageNumber, pageSize));
    }


    @PostMapping("/toggle-reaction")
    Mono<ResponseEntity<Void>> toggleReaction(@RequestBody Reaction reaction) {
        return service.handleReaction(reaction).map(ResponseEntity::ok);
    }


    @GetMapping("/count")
    Mono<ResponseEntity<Integer>> countByContentIdAndReactionType(@RequestParam String contentId, @RequestParam ReactionType type) {
        return service.countByContentIdAndType(contentId, type).map(ResponseEntity::ok);
    }

}
