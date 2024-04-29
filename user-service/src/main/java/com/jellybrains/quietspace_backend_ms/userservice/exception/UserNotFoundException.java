package com.jellybrains.quietspace_backend_ms.userservice.exception;

import jakarta.ws.rs.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super();
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}
