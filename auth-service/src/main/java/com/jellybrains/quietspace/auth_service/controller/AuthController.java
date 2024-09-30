package com.jellybrains.quietspace.auth_service.controller;

import com.jellybrains.quietspace.auth_service.model.AuthRequest;
import com.jellybrains.quietspace.auth_service.model.AuthResponse;
import com.jellybrains.quietspace.auth_service.model.RegistrationRequest;
import com.jellybrains.quietspace.auth_service.service.impls.AuthService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    public static final String AUTH_PATH = "/api/v1/auth";
    private final AuthService authService;


    @CircuitBreaker(name = "auth-service")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Validated RegistrationRequest request) throws MessagingException {
        authService.requestUserRegistration(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    @CircuitBreaker(name = "auth-service")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/activate-account")
    @CircuitBreaker(name = "auth-service")
    public ResponseEntity<?> confirm(@RequestParam String token) throws MessagingException {
        authService.activateAccount(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signout")
    @CircuitBreaker(name = "auth-service")
    ResponseEntity<?> signout(@RequestHeader("Authorization") String authHeader) {
        authService.signout(authHeader);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    @CircuitBreaker(name = "auth-service")
    ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(authService.refreshToken(authHeader));
    }

    @PostMapping("/resend-code")
    @CircuitBreaker(name = "auth-service")
    ResponseEntity<?> resendActivationEmail(@RequestParam String email) throws MessagingException {
        authService.resendActivationToken(email);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    @CircuitBreaker(name = "auth-service")
    public ResponseEntity<Void> getCurrentUser(Authentication auth, @PathVariable String userId) {
        authService.requestUserDeletionById(auth, userId);
        return ResponseEntity.accepted().build();
    }

}
