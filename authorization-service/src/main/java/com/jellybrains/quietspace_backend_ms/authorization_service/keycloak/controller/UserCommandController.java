package com.jellybrains.quietspace_backend_ms.authorization_service.keycloak.controller;

import com.jellybrains.quietspace_backend_ms.authorization_service.keycloak.service.UserCommandService;
import com.jellybrains.quietspace_backend_ms.authorization_service.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserCommandController {

    private final UserCommandService userService;

    @DeleteMapping("/delete")
    public Mono<Void> deleteAuthenticatedUser(@AuthenticationPrincipal Jwt jwt) {
        String id = JwtUtil.getUserFromToken(jwt).getId();
        return userService.requestUserDeletionById(UUID.fromString(id));
    }

    @PutMapping("/change-password")
    public Mono<Void> changePassword(@AuthenticationPrincipal Jwt jwt) {
        String id = JwtUtil.getUserFromToken(jwt).getId();
        return userService.updatePassword(id);
    }

}


