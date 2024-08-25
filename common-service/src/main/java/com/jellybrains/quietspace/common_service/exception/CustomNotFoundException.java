package com.jellybrains.quietspace.common_service.exception;


public class CustomNotFoundException extends RuntimeException {
    public CustomNotFoundException(){
        super("resource not found");
    }
    public CustomNotFoundException(String message) {
        super(message);
    }
}
