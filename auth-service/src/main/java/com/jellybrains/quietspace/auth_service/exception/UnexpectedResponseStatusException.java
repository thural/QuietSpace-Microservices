package com.jellybrains.quietspace.auth_service.exception;

public class UnexpectedResponseStatusException extends RuntimeException {
    public UnexpectedResponseStatusException(int status) {
        super("Unexpected response status: " + status);
    }
}
