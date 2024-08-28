package com.jellybrains.quietspace.feed_service.exception;


public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("user not found");
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}
