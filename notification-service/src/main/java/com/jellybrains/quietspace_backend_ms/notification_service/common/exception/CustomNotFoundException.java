package com.jellybrains.quietspace_backend_ms.notification_service.common.exception;


public class CustomNotFoundException extends RuntimeException {
    public CustomNotFoundException(){
        super();
    }
    public CustomNotFoundException(String message) {
        super(message);
    }
}
