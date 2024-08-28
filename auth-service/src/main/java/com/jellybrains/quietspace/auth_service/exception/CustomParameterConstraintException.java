package com.jellybrains.quietspace.auth_service.exception;

public class CustomParameterConstraintException extends RuntimeException {
    public CustomParameterConstraintException() {
        super();
    }
    public CustomParameterConstraintException(String message) {
        super(message);
    }
}
