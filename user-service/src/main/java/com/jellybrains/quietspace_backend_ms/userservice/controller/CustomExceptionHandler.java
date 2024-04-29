package com.jellybrains.quietspace_backend_ms.userservice.controller;

import com.jellybrains.quietspace_backend_ms.userservice.dto.response.ErrorResponse;
import com.jellybrains.quietspace_backend_ms.userservice.exception.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> handleBindErrors(MethodArgumentNotValidException exception) {
        List<Map<String, String>> errorList = exception.getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                    return errorMap;
                }).collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errorList);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(RuntimeException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(ErrorResponse.builder()
                .code(400)
                .status(status.name())
                .message(e.getMessage())
                .build(), status);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundExceptions(RuntimeException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(status.name())
                        .message("the requested resource not found: " + e.getMessage())
                        .code(404)
                        .timestamp(new Date())
                        .build(), status);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleExceptions(RuntimeException exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.internalServerError()
                .body(ErrorResponse.builder()
                        .code(500)
                        .status(status.name())
                        .message("An unexpected error occurred: " + exception.getMessage())
                        .timestamp(new Date())
                        .build()
                );
    }


}
