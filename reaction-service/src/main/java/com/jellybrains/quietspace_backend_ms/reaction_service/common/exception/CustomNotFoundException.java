package com.jellybrains.quietspace_backend_ms.reaction_service.common.exception;


public class CustomNotFoundException extends RuntimeException {
    public CustomNotFoundException(){
        super();
    }
    public CustomNotFoundException(String message) {
        super(message);
    }
}
