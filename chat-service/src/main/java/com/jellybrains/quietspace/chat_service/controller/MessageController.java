package com.jellybrains.quietspace.chat_service.controller;

import com.jellybrains.quietspace.chat_service.service.MessageService;
import com.jellybrains.quietspace.common_service.model.request.MessageRequest;
import com.jellybrains.quietspace.common_service.model.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageService messageService;


    @PostMapping
    Mono<ResponseEntity<MessageResponse>> createMessage(@RequestBody @Validated MessageRequest messageRequest) {
        return messageService.addMessage(messageRequest).map(ResponseEntity::ok);
    }


    @DeleteMapping("/{messageId}")
    Mono<ResponseEntity<MessageResponse>> deleteMessage(@PathVariable String messageId) {
        return messageService.deleteMessage(messageId).map(ResponseEntity::ok);
    }


    @GetMapping("/chat/{chatId}")
    Flux<MessageResponse> getMessagesByChatId(
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize,
            @PathVariable String chatId
    ) {
        return messageService.getMessagesByChatId(pageNumber, pageSize, chatId);
    }

}
