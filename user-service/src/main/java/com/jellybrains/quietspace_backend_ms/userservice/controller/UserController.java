package com.jellybrains.quietspace_backend_ms.userservice.controller;

import com.jellybrains.quietspace_backend_ms.userservice.dto.requuest.UserRequest;
import com.jellybrains.quietspace_backend_ms.userservice.dto.response.UserResponse;
import com.jellybrains.quietspace_backend_ms.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserRequest user) {
        userService.createUser(user);
    }

    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserByEmail(@PathVariable String email){

        return userService.findUserByEmail(email);
    }
}
