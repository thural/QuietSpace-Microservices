package com.jellybrains.quietspace_backend_ms.feedservice.exception;

import jakarta.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException() {
        super("user not found");
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}
