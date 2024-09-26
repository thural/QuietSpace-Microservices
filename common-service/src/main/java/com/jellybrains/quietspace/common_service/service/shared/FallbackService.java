package com.jellybrains.quietspace.common_service.service.shared;

import java.util.concurrent.CompletableFuture;

public class FallbackService {

    public static CompletableFuture<String> fallbackMethod(Throwable exception) {
        return CompletableFuture.supplyAsync(() ->
                String.format("requested service is temporally unavailable: %s", exception.getMessage())
        );
    }
}
