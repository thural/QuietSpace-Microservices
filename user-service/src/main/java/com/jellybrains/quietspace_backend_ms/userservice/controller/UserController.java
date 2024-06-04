package com.jellybrains.quietspace_backend_ms.userservice.controller;

import com.jellybrains.quietspace_backend_ms.userservice.model.request.UserRequest;
import com.jellybrains.quietspace_backend_ms.userservice.model.response.UserResponse;
import com.jellybrains.quietspace_backend_ms.userservice.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    public static final String USER_PATH_ID = "/{userId}";

    private final UserService userService;

    @GetMapping
    Page<UserResponse> listUsers(@RequestParam(name = "username", required = false) String username,
                                 @RequestParam(name = "page-number", required = false) Integer pageNumber,
                                 @RequestParam(name = "page-size", required = false) Integer pageSize) {
        return userService.listUsers(username, pageNumber, pageSize);
    }

    @GetMapping("/search")
    Page<UserResponse> listUsersByQuery(@RequestParam(name = "query", required = false) String query,
                                        @RequestParam(name = "page-number", required = false) Integer pageNumber,
                                        @RequestParam(name = "page-size", required = false) Integer pageSize) {
        return userService.listUsersByQuery(query, pageNumber, pageSize);
    }

    @GetMapping(USER_PATH_ID)
    ResponseEntity<?> getUserById(@PathVariable UUID userId) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return userService.getUserResponseById(userId)
                .map(user -> ResponseEntity.ok().headers(headers).body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(USER_PATH_ID)
    ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String authHeader,
                                 @PathVariable UUID userId) {
        userService.deleteUser(userId, authHeader);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping
    ResponseEntity<?> patchUser(@RequestBody UserRequest userRequest) {
        UserResponse patchedUser = userService.patchUser(userRequest);
        return new ResponseEntity<>(patchedUser, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public UserResponse getUserFromToken() {
        return userService.getLoggedUserResponse().orElse(null);
    }

    @GetMapping("/validate/{userId}")
    public ResponseEntity<Boolean> validateUserInRequest(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.validateUserInRequest(userId));
    }

    @GetMapping("/validate/list")
    public ResponseEntity<Boolean> validateUserList(@RequestParam List<UUID> userIds) {
        return ResponseEntity.ok(userService.validateUsersByIdList(userIds));
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getAuthenticatedUser() {
        return userService.getLoggedUserResponse()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
