package com.jellybrains.quietspace_backend_ms.userservice.controller;

import com.jellybrains.quietspace_backend_ms.userservice.common.model.request.CreateProfileRequest;
import com.jellybrains.quietspace_backend_ms.userservice.common.model.response.ProfileResponse;
import com.jellybrains.quietspace_backend_ms.userservice.common.model.response.UserResponse;
import com.jellybrains.quietspace_backend_ms.userservice.entity.Profile;
import com.jellybrains.quietspace_backend_ms.userservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class ProfileController {

    public static final String USER_PATH = "/api/v1/users";
    public static final String USER_PATH_ID = "/{userId}";
    public static final String FOLLOW_PATH_ID = "/follow/{userId}";
    public static final String ONLINE_USERS_PATH = "/user/onlineUsers";
    public static final String FOLLOW_USER_TOGGLE_PATH = FOLLOW_PATH_ID + "/toggle-follow";


    private final ProfileService profileService;

    @GetMapping("/search")
    Page<UserResponse> listUsersByQuery(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return profileService.listUsersByQuery(query, pageNumber, pageSize);
    }

    @GetMapping("/getUsersFromList")
    public List<UserResponse> getUserResponseFromIdList(@RequestBody List<String> userIds) {
        return profileService.getUsersFromIdList(userIds);
    }

    @GetMapping(USER_PATH_ID)
    ResponseEntity<UserResponse> getUserById(@PathVariable String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return profileService.getUserResponseById(userId)
                .map(user -> ResponseEntity.ok().headers(headers).body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping
    private ResponseEntity<ProfileResponse> patchProfile(@RequestBody CreateProfileRequest userRequest) {
        ProfileResponse patchedUser = profileService.patchProfile(userRequest);
        return ResponseEntity.ok(patchedUser);
    }

    @GetMapping("/profile")
    public Profile loadUserProfile() {
        return profileService.getUserProfile();
    }

    @GetMapping("/validate/{userId}")
    public ResponseEntity<Boolean> validateUserInRequest(@PathVariable String userId) {
        return ResponseEntity.ok(profileService.validateUserInRequest(userId));
    }

    @GetMapping("/validate/list")
    public ResponseEntity<Boolean> validateUserList(@RequestParam List<String> userIds) {
        return ResponseEntity.ok(profileService.validateUsersByIdList(userIds));
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getAuthenticatedUser() {
        return profileService.getSignedUserResponse()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
