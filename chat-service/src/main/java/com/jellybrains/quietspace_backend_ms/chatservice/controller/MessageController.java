package com.jellybrains.quietspace_backend_ms.chatservice.controller;

import com.jellybrains.quietspace_backend_ms.chatservice.model.request.MessageRequest;
import com.jellybrains.quietspace_backend_ms.chatservice.model.response.MessageResponse;
import com.jellybrains.quietspace_backend_ms.chatservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/messages")
public class MessageController {

    public static final String MESSAGE_PATH_ID = "/{messageId}";

    private final MessageService messageService;


    @PostMapping
    ResponseEntity<?> createMessage(@RequestBody @Validated MessageRequest messageRequest) {
        MessageResponse createdMessage = messageService.addMessage(messageRequest);
        return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
    }

    @DeleteMapping(MESSAGE_PATH_ID)
    ResponseEntity<?> deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/chat/{chatId}")
    Page<MessageResponse> getMessagesByChatId(
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize,
            @PathVariable UUID chatId) {
        return messageService.getMessagesByChatId(pageNumber, pageSize, chatId);
    }

}
