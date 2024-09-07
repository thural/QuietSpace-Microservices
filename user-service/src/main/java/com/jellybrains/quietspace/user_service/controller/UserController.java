package com.jellybrains.quietspace.user_service.controller;

import com.jellybrains.quietspace.common_service.model.response.UserResponse;
import com.jellybrains.quietspace.common_service.webclient.service.UserService;
import com.jellybrains.quietspace.user_service.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    public static final String USER_PATH = "/api/v1/user";
    public static final String USER_PATH_ID = "/{userId}";
    public static final String ONLINE_USERS_PATH = "/user/onlineUsers";

    private final ProfileService profileService;
    private final UserService userService;


    @GetMapping("/search")
    Page<UserResponse> listUsersByQuery(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return profileService.listUsersByQuery(query, pageNumber, pageSize);
    }


    @GetMapping("/getUsersFromList")
    public ResponseEntity<List<UserResponse>> getUserResponseFromIdList(@RequestBody List<String> userIds) {
        return ResponseEntity.ok(profileService.getUsersFromIdList(userIds));
    }


    @GetMapping("/{userId}")
    ResponseEntity<UserResponse> getUserById(@PathVariable String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return profileService.getUserResponseById(userId)
                .map(user -> ResponseEntity.ok().headers(headers).body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping
    public ResponseEntity<UserResponse> getAuthenticatedUser() {
        return profileService.getSignedUserResponse()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/validate/{userId}")
    public ResponseEntity<Boolean> validateUserInRequest(@PathVariable String userId) {
        return ResponseEntity.ok(profileService.validateUserInRequest(userId));
    }


    @GetMapping("/validate/list")
    public ResponseEntity<Boolean> validateUserList(@RequestParam List<String> userIds) {
        return ResponseEntity.ok(profileService.validateUsersByIdList(userIds));
    }


    @GetMapping("/claims")
    Map<String, String> getClaims() {
        Map<String, String> claims = new HashMap<>();
        String userId = userService.getAuthorizedUserId();
        String username = userService.getAuthorizedUsername();
        String userFullName = userService.getAuthorizedUserFullName();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("userFullName", userFullName);
        return claims;
    }

}