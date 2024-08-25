package com.jellybrains.quietspace_backend_ms.userservice.common.exception;

public class CustomDataNotFoundException extends RuntimeException{
    public CustomDataNotFoundException(String message) {
        super(message);
    }
}
