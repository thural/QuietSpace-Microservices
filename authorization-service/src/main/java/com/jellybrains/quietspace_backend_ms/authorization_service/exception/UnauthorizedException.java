package com.jellybrains.quietspace_backend_ms.authorization_service.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

