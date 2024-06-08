package com.jellybrains.quietspace_backend_ms.authorization_service.command.controller;

import com.jellybrains.quietspace_backend_ms.authorization_service.command.controller.model.CreateUserCommand;
import com.jellybrains.quietspace_backend_ms.authorization_service.command.service.UserCommandService;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/public/users")
@RequiredArgsConstructor
public class UserPublicCommandController {

    private final UserCommandService userService;

//    @PostMapping("/create-user")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Mono<User> createPendingUser(@RequestBody CreateUserCommand request) {
//        return userService.createUser(request);
//    }

}
