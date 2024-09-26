package com.jellybrains.quietspace.common_service.service.shared;

public class FallbackService {

    public static String fallbackMethod(Throwable exception) {
        return String.format("requested service is temporally unavailable: %s", exception.getMessage());
    }
}
