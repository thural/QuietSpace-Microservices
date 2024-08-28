package com.jellybrains.quietspace.auth_service.exception;

public class ActivationTokenException extends RuntimeException {
    public ActivationTokenException(String message) {
        super(message);
    }
}
