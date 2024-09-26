package com.jellybrains.quietspace.common_service.exception;

public class UnexpectedResponseStatusException extends RuntimeException {
    public UnexpectedResponseStatusException(int status) {
        super("Unexpected response status: " + status);
    }
}
