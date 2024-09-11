package com.jellybrains.quietspace.chat_service.controller;

import com.jellybrains.quietspace.chat_service.service.ChatService;
import com.jellybrains.quietspace.common_service.model.request.ChatRequest;
import com.jellybrains.quietspace.common_service.model.response.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/{chatId}")
    Mono<ResponseEntity<ChatResponse>> getSingleChatById(@PathVariable String chatId) {
        return chatService.getChatById(chatId).map(ResponseEntity::ok);
    }


    @GetMapping("/members/{userId}")
    ResponseEntity<Flux<ChatResponse>> getChatsByMemberId(@PathVariable String userId) {
        return ResponseEntity.ok(chatService.getChatsByUserId(userId));
    }


    @PostMapping
    Mono<ResponseEntity<ChatResponse>> createChat(@RequestBody ChatRequest chat) {
        return chatService.createChat(chat).map(ResponseEntity::ok);
    }


    @PatchMapping("/{chatId}/members/add/{userId}")
    ResponseEntity<Flux<String>> addMemberWithId(@PathVariable String userId, @PathVariable String chatId) {
        return ResponseEntity.ok(chatService.addMemberWithId(userId, chatId));
    }


    @PatchMapping("/{chatId}/members/remove/{userId}")
    ResponseEntity<Flux<String>> removeMemberWithId(@PathVariable String chatId, @PathVariable String userId) {
        return ResponseEntity.ok(chatService.removeMemberWithId(userId, chatId));
    }

    
    @DeleteMapping("/{chatId}")
    Mono<ResponseEntity<Void>> deleteChatWithId(@PathVariable("chatId") String chatId) {
        return chatService.deleteChatById(chatId).map(ResponseEntity::ok);
    }

}
