package com.jellybrains.quietspace_backend_ms.reaction_service.common.exception;

public class CustomDataNotFoundException extends RuntimeException{
    public CustomDataNotFoundException(String message) {
        super(message);
    }
}
