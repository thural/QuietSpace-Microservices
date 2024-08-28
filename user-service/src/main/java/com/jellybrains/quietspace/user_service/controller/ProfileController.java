package com.jellybrains.quietspace.user_service.controller;

import com.jellybrains.quietspace.common_service.enums.NotificationType;
import com.jellybrains.quietspace.common_service.model.request.CreateProfileRequest;
import com.jellybrains.quietspace.common_service.model.response.ProfileResponse;
import com.jellybrains.quietspace.common_service.model.response.UserResponse;
import com.jellybrains.quietspace.user_service.entity.Profile;
import com.jellybrains.quietspace.user_service.service.ProfileService;
import com.jellybrains.quietspace.user_service.webclient.client.NotificationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileController {

    public static final String FOLLOW_PATH_ID = "/follow/{userId}";
    public static final String FOLLOW_USER_TOGGLE_PATH = FOLLOW_PATH_ID + "/toggle-follow";

    private final ProfileService profileService;
    private final NotificationClient notificationClient;



    @PatchMapping
    private ResponseEntity<ProfileResponse> patchProfile(@RequestBody CreateProfileRequest userRequest) {
        ProfileResponse patchedUser = profileService.patchProfile(userRequest);
        return ResponseEntity.ok(patchedUser);
    }


    @GetMapping
    public ResponseEntity<Profile> loadUserProfile() {
        return ResponseEntity.ok(profileService.getUserProfile());
    }


    @GetMapping("/followings")
    Page<UserResponse> listFollowings(
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return profileService.listFollowings(pageNumber, pageSize);
    }


    @GetMapping("/followers")
    Page<UserResponse> listFollowers(
            @RequestParam(name = "page-number", required = false) Integer pageNumber,
            @RequestParam(name = "page-size", required = false) Integer pageSize
    ) {
        return profileService.listFollowers(pageNumber, pageSize);
    }


    @PostMapping(FOLLOW_USER_TOGGLE_PATH)
    ResponseEntity<Void> toggleFollow(@PathVariable String userId) {
        profileService.toggleFollow(userId);
        notificationClient.processNotification(NotificationType.FOLLOW_REQUEST, userId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/followers/remove/{userId}")
    ResponseEntity<Void> removeFollower(@PathVariable String userId) {
        profileService.removeFollower(userId);
        return ResponseEntity.ok().build();
    }

}
