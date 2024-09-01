package com.jellybrains.quietspace.chat_service.controller;

import com.jellybrains.quietspace.chat_service.service.MessageService;
import com.jellybrains.quietspace.common_service.model.request.MessageRequest;
import com.jellybrains.quietspace.common_service.model.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/messages")
public class MessageController {

    public static final String MESSAGE_PATH_ID = "/{messageId}";

    private final MessageService messageService;


    @PostMapping
    ResponseEntity<MessageResponse> createMessage(@RequestBody @Validated MessageRequest messageRequest) {
        MessageResponse createdMessage = messageService.addMessage(messageRequest);
        return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
    }

    @DeleteMapping(MESSAGE_PATH_ID)
    ResponseEntity<Void> deleteMessage(@PathVariable String messageId) {
        messageService.deleteMessage(messageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/chat/{chatId}")
    Page<MessageResponse> getMessagesByChatId(
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize,
            @PathVariable String chatId) {
        return messageService.getMessagesByChatId(pageNumber, pageSize, chatId);
    }

}
