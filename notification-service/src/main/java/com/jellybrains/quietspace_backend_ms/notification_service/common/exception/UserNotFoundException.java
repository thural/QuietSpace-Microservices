package com.jellybrains.quietspace_backend_ms.notification_service.common.exception;


public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("user not found");
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}
