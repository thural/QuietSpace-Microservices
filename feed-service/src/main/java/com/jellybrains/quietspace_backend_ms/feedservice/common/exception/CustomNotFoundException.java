package com.jellybrains.quietspace_backend_ms.feedservice.common.exception;

import jakarta.persistence.EntityNotFoundException;

public class CustomNotFoundException extends EntityNotFoundException {
    public CustomNotFoundException(){
        super();
    }
    public CustomNotFoundException(String message) {
        super(message);
    }
}
