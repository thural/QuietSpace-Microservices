package com.jellybrains.quietspace_backend_ms.dummy_service.controller;

import com.jellybrains.quietspace_backend_ms.dummy_service.dto.request.DummyRequest;
import com.jellybrains.quietspace_backend_ms.dummy_service.service.DummyService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dummy")
public class HelloController {

    private final DummyService dummyService;

    @GetMapping("/hello")
    @CircuitBreaker(name = "dummy", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "dummy")
    @Retry(name = "dummy")
    public CompletableFuture<String> hello(@RequestBody DummyRequest dummyRequest) {
        return CompletableFuture.supplyAsync(() -> dummyService.buildMessage(dummyRequest));
    }

    public CompletableFuture<String> fallbackMethod(DummyRequest dummyRequest, RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(() -> "Something went wrong, please try later");
    }
}
