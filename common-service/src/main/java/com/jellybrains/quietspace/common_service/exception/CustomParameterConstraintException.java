package com.jellybrains.quietspace.common_service.exception;

public class CustomParameterConstraintException extends RuntimeException {
    public CustomParameterConstraintException() {
        super();
    }
    public CustomParameterConstraintException(String message) {
        super(message);
    }
}
