package com.jellybrains.quietspace.common_service.service.shared;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

@Slf4j
public class FallbackService {

    public static CompletableFuture<String> fallbackMethod(Throwable exception) {
        return CompletableFuture.supplyAsync(() ->
                String.format("requested service is temporally unavailable: %s", exception.getMessage())
        );
    }

    public String defaultString(Exception e) {
        log.info("an exception is thrown: {}", e.getMessage());
        return "operation failure: " + e.getMessage();
    }

    public String defaultString(TimeoutException e) {
        log.info("a timeout exception is thrown: {}", e.getMessage());
        return "operation timeout: " + e.getMessage();
    }


    public Mono<String> monoString(Exception e) {
        log.info("an exception is thrown by mono: {}", e.getMessage());
        return Mono.just("operation failure: " + e.getMessage());
    }

    public Mono<String> monoString(TimeoutException e) {
        log.info("a timeout exception is thrown by mono: {}", e.getMessage());
        return Mono.just("operation timeout: " + e.getMessage());
    }


    public Flux<String> fluxString(Exception e) {
        log.info("an exception is thrown by flux: {}", e.getMessage());
        return Flux.just("operation failure: " + e.getMessage());
    }

    public Flux<String> fluxString(TimeoutException e) {
        log.info("a timeout exception is thrown by flex: {}", e.getMessage());
        return Flux.just("operation timeout for flux: " + e.getMessage());
    }


    public CompletableFuture<String> futureString(Exception e) {
        log.info("an exception is thrown by future: {}", e.getMessage());
        return CompletableFuture.completedFuture("an error occurred: " + e.getMessage());
    }

    public CompletableFuture<String> futureString(TimeoutException e) {
        log.info("a timeout exception is thrown by future: {}", e.getMessage());
        return CompletableFuture.completedFuture("operation timeout: " + e.getMessage());
    }
}
