package com.jellybrains.quietspace.dummy_service.controller;

import com.jellybrains.quietspace.common_service.message.kafka.chat.request.SendMessageRequest;
import com.jellybrains.quietspace.common_service.message.kafka.user.UserCreationEvent;
import com.jellybrains.quietspace.common_service.model.request.MessageRequest;
import com.jellybrains.quietspace.common_service.websocket.model.UserRepresentation;
import com.jellybrains.quietspace.dummy_service.kafka.producer.ChatProducer;
import com.jellybrains.quietspace.dummy_service.kafka.producer.UserProducer;
import com.jellybrains.quietspace.dummy_service.model.request.DummyRequest;
import com.jellybrains.quietspace.dummy_service.service.DummyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dummy")
public class HelloController {

    private final DummyService dummyService;
    private final UserProducer userProducer;
    private final ChatProducer chatProducer;

    @GetMapping("/produce-message")
    public void produceChatMessageEvent() {

        log.info("producing a chat message as kafka event...");

        chatProducer.chatMessageRequest(SendMessageRequest.builder()
                .eventBody(MessageRequest.builder()
                        .text("test message event from dummy controller")
                        .build())
                .build());
    }

    @GetMapping("/produce-user")
    public void sendSampleUser() {
        var user = UserRepresentation.builder()
                .username("tommy")
                .lastname("shelby")
                .email("thommmy@email.com")
                .build();
        userProducer.userCreation(
                UserCreationEvent.builder()
                        .userId(UUID.randomUUID().toString())
                        .eventBody(user)
                        .message("created a test user")
                        .build()
        );
    }

//    @GetMapping("/hello")
//    @CircuitBreaker(name = "dummy", fallbackMethod = "fallbackMethod")
//    @TimeLimiter(name = "dummy")
//    @Retry(name = "dummy")
//    public CompletableFuture<String> hello(@RequestBody DummyRequest dummyRequest) {
//        return CompletableFuture.supplyAsync(() -> dummyService.buildMessage(dummyRequest));
//    }

    @GetMapping("/hello")
    public String hello() {
        return "hello from dummy controller";
    }

    public CompletableFuture<String> fallbackMethod(DummyRequest dummyRequest, RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(() -> "Something went wrong, please try later");
    }
}
