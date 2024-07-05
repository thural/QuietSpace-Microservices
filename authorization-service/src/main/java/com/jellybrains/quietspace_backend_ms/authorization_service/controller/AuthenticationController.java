package com.jellybrains.quietspace_backend_ms.authorization_service.controller;

import com.jellybrains.quietspace_backend_ms.authorization_service.model.request.AuthenticationRequest;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.request.RegistrationRequest;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.response.AuthenticationResponse;
import com.jellybrains.quietspace_backend_ms.authorization_service.service.impls.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) throws MessagingException {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/activate-account")
    public void confirm(@RequestParam String token) throws MessagingException {
        service.activateAccount(token);
    }

    @PostMapping("/signout")
    ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String authHeader) {
        // TODO: implement logout method
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
