package com.jellybrains.quietspace_backend_ms.authorization_service.query.controller;

import com.jellybrains.quietspace_backend_ms.authorization_service.model.User;
import com.jellybrains.quietspace_backend_ms.authorization_service.query.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/users")
@RequiredArgsConstructor
public class UserPublicQueryController {

    private final UserQueryService userService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<User> getUserById(@PathVariable String id) {
        return Mono.just(userService.getUserById(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<User>> getUsers() {
        return Mono.just(userService.getUsers());
    }
}
