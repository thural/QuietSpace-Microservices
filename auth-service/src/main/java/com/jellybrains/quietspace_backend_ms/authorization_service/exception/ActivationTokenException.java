package com.jellybrains.quietspace_backend_ms.authorization_service.exception;

public class ActivationTokenException extends RuntimeException {
    public ActivationTokenException(String message) {
        super(message);
    }
}
