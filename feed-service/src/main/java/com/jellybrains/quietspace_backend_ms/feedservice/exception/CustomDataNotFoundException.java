package com.jellybrains.quietspace_backend_ms.feedservice.exception;

public class CustomDataNotFoundException extends RuntimeException{
    public CustomDataNotFoundException(String message) {
        super(message);
    }
}
