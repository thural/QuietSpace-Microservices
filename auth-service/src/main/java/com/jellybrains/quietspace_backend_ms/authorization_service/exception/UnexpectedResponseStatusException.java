package com.jellybrains.quietspace_backend_ms.authorization_service.exception;

public class UnexpectedResponseStatusException extends RuntimeException {
    public UnexpectedResponseStatusException(int status) {
        super("Unexpected response status: " + status);
    }
}
