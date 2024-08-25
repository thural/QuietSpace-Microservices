package com.jellybrains.quietspace_backend_ms.userservice.common.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

