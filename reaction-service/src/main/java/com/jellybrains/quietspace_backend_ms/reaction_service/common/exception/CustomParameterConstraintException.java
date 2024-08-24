package com.jellybrains.quietspace_backend_ms.reaction_service.common.exception;

public class CustomParameterConstraintException extends RuntimeException {
    public CustomParameterConstraintException() {
        super();
    }
    public CustomParameterConstraintException(String message) {
        super(message);
    }
}
