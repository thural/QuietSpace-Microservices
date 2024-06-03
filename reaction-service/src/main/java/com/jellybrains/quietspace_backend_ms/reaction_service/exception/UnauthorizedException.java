package com.jellybrains.quietspace_backend_ms.reaction_service.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

