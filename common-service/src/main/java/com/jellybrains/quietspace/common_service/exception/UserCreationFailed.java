package com.jellybrains.quietspace.common_service.exception;

public class UserCreationFailed extends RuntimeException {
    public UserCreationFailed() {
        super("User creation failed");
    }

    public UserCreationFailed(String message) {
        super(message);
    }
}
