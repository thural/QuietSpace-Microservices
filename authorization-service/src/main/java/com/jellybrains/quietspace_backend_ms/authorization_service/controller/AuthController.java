package com.jellybrains.quietspace_backend_ms.authorization_service.controller;

import com.jellybrains.quietspace_backend_ms.authorization_service.model.request.LoginRequest;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.request.UserRequest;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.response.AuthResponse;
import com.jellybrains.quietspace_backend_ms.authorization_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    ResponseEntity<?> signupUser(@Validated @RequestBody UserRequest user) {
        AuthResponse authResponse = authService.register(user);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {

        AuthResponse authResponse = authService.login(loginRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);

    }

    @PostMapping("/logout")
    ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String authHeader) {
        authService.logout(authHeader);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/hello")
    ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello Auth!");
    }

}
