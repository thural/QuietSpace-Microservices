package com.jellybrains.quietspace_backend_ms.userservice.service;

import com.jellybrains.quietspace_backend_ms.userservice.common.enums.StatusType;
import com.jellybrains.quietspace_backend_ms.userservice.common.model.request.CreateProfileRequest;
import com.jellybrains.quietspace_backend_ms.userservice.common.model.response.ProfileResponse;
import com.jellybrains.quietspace_backend_ms.userservice.common.model.response.UserResponse;
import com.jellybrains.quietspace_backend_ms.userservice.entity.Profile;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProfileService {

    Page<UserResponse> listUsersByQuery(String query, Integer pageNumber, Integer pageSize);

    List<UserResponse> getUsersFromIdList(List<String> userIds);

    Optional<UserResponse> getUserResponseById(String id);

    Optional<Profile> getProfileById(String userId);

    void deleteProfileById(String userId);

    ProfileResponse patchProfile(CreateProfileRequest request);

    Optional<UserResponse> getSignedUserResponse();

    Page<UserResponse> listFollowings(Integer pageNumber, Integer pageSize);

    Page<UserResponse> listFollowers(Integer pageNumber, Integer pageSize);

    void toggleFollow(String followedUserId);

    Profile getUserProfile();

    void removeFollower(String followingUserId);

    void setOnlineStatus(String userEmail, StatusType type);

    List<String> findConnectedFollowings();

    Boolean validateUserInRequest(String userId);

    Boolean validateUsersByIdList(List<String> userIds);
}
