package com.jellybrains.quietspace_backend_ms.authorization_service.exception;

public class CustomParameterConstraintException extends RuntimeException {
    public CustomParameterConstraintException() {
        super();
    }

    public CustomParameterConstraintException(String message) {
        super("A parameter constraint error occurred: ".concat(message));
    }
}
