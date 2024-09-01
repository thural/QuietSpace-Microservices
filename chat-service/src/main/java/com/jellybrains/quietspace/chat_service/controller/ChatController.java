package com.jellybrains.quietspace.chat_service.controller;

import com.jellybrains.quietspace.chat_service.repository.MessageRepository;
import com.jellybrains.quietspace.chat_service.service.ChatService;
import com.jellybrains.quietspace.chat_service.service.MessageService;
import com.jellybrains.quietspace.common_service.model.request.ChatRequest;
import com.jellybrains.quietspace.common_service.model.response.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate template;
    private final MessageService messageService;

    @GetMapping("/{chatId}")
    ResponseEntity<ChatResponse> getSingleChatById(@PathVariable String chatId) {
        return ResponseEntity.ok(chatService.getChatById(chatId));
    }

    @GetMapping("/members/{userId}")
    ResponseEntity<List<ChatResponse>> getChatsByMemberId(@PathVariable String userId) {
        return ResponseEntity.ok(chatService.getChatsByUserId(userId));
    }

    @PostMapping
    ResponseEntity<ChatResponse> createChat(@RequestBody ChatRequest chat) {
        return ResponseEntity.ok(chatService.createChat(chat));
    }

    @PatchMapping("/{chatId}/members/add/{userId}")
    ResponseEntity<?> addMemberWithId(@PathVariable String userId, @PathVariable String chatId) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{chatId}/members/remove/{userId}")
    ResponseEntity<?> removeMemberWithId(@PathVariable String chatId, @PathVariable String userId) {
        chatService.removeMemberWithId(userId, chatId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{chatId}")
    ResponseEntity<?> deleteChatWithId(@PathVariable("chatId") String chatId) {
        chatService.deleteChatById(chatId);
        return ResponseEntity.noContent().build();
    }

}
