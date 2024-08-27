package com.jellybrains.quietspace_backend_ms.authorization_service.controller;

import com.jellybrains.quietspace_backend_ms.authorization_service.model.AuthRequest;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.AuthResponse;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.RegistrationRequest;
import com.jellybrains.quietspace_backend_ms.authorization_service.service.impls.AuthService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated RegistrationRequest request) throws MessagingException {
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/activate-account")
    public ResponseEntity<?> confirm(@RequestParam String token) throws MessagingException {
        authService.activateAccount(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signout")
    ResponseEntity<?> signout(@RequestHeader("Authorization") String authHeader) {
        authService.signout(authHeader);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(authService.refreshToken(authHeader));
    }

    @PostMapping("/resend-code")
    ResponseEntity<?> resendActivationEmail(@RequestParam String email) throws MessagingException {
        authService.resendActivationToken(email);
        return ResponseEntity.ok().build();
    }

}
