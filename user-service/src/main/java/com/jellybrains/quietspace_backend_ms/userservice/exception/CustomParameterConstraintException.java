package com.jellybrains.quietspace_backend_ms.userservice.exception;

public class CustomParameterConstraintException extends RuntimeException {
    public CustomParameterConstraintException() {
        super();
    }
    public CustomParameterConstraintException(String message) {
        super(message);
    }
}
