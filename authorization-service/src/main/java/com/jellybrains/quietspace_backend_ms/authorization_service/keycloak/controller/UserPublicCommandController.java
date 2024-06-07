package com.jellybrains.quietspace_backend_ms.authorization_service.keycloak.controller;

import com.jellybrains.quietspace_backend_ms.authorization_service.keycloak.model.CreateUserCommand;
import com.jellybrains.quietspace_backend_ms.authorization_service.keycloak.model.User;
import com.jellybrains.quietspace_backend_ms.authorization_service.keycloak.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/public/users")
@RequiredArgsConstructor
public class UserPublicCommandController {

    private final UserCommandService userService;

    @PostMapping("/create-user")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> createPendingUser(@RequestBody CreateUserCommand request) {
        return userService.createUser(request);
    }

}
