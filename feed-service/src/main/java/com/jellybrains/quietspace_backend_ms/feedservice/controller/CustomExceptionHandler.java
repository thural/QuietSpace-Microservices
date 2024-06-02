package com.jellybrains.quietspace_backend_ms.feedservice.controller;

import com.jellybrains.quietspace_backend_ms.feedservice.exception.*;
import com.jellybrains.quietspace_backend_ms.feedservice.model.response.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    ResponseEntity<?> handleJPAViolations(TransactionSystemException exception) {
        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();

        if (exception.getCause().getCause() instanceof ConstraintViolationException violationException) {

            List<Map<String, String>> errors = violationException.getConstraintViolations().stream()
                    .map(constraintViolation -> {
                        Map<String, String> errorMap = new HashMap<>();
                        errorMap.put(constraintViolation.getPropertyPath().toString(),
                                constraintViolation.getMessage());
                        return errorMap;
                    }).collect(Collectors.toList());

            return responseEntity.body(errors);
        }

        return responseEntity.build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors()
                .forEach(objectError -> errors.put(objectError.getDefaultMessage(), objectError.getDefaultMessage()));

        return responseEntity.body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(RuntimeException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(ErrorResponse.builder()
                .code(status.value())
                .status(status.name())
                .message(e.getMessage())
                .build(), status);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(RuntimeException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(ErrorResponse.builder()
                .code(status.value())
                .status(status.name())
                .message(e.getMessage())
                .build(), status);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(RuntimeException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        return new ResponseEntity<>(ErrorResponse.builder()
                .code(status.value())
                .status(status.name())
                .message(e.getMessage())
                .build(), status);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(RuntimeException e) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        return new ResponseEntity<>(ErrorResponse.builder()
                .code(status.value())
                .status(status.name())
                .message(e.getMessage())
                .build(), status);
    }


    @ExceptionHandler(CustomDataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomDataNotFoundExceptions(RuntimeException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(ErrorResponse.builder()
                .code(status.value())
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
                        .code(status.value())
                        .timestamp(new Date())
                        .build(), status);
    }

    @ExceptionHandler(CustomParameterConstraintException.class)
    public ResponseEntity<ErrorResponse> handleCustomParameterConstraintExceptions(RuntimeException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code(status.value())
                        .message("A parameter constraint error occurred: " + e.getMessage())
                        .status(status.name())
                        .build(),
                status);
    }

    @ExceptionHandler(CustomErrorException.class)
    public ResponseEntity<ErrorResponse> handleCustomErrorExceptions(RuntimeException e) {
        CustomErrorException customErrorException = (CustomErrorException) e;

        HttpStatus status = customErrorException.getStatus();

        return ResponseEntity.internalServerError()
                .body(ErrorResponse.builder()
                        .code(status.value())
                        .status(status.name())
                        .message("An unexpected error occurred: " + e.getMessage())
                        .timestamp(new Date())
                        .build()
                );
    }

}
