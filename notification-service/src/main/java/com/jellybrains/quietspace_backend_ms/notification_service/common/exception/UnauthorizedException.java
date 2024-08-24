package com.jellybrains.quietspace_backend_ms.notification_service.common.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

