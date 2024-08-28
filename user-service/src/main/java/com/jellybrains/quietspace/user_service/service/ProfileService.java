package com.jellybrains.quietspace.user_service.service;


import com.jellybrains.quietspace.common_service.enums.StatusType;
import com.jellybrains.quietspace.common_service.model.request.CreateProfileRequest;
import com.jellybrains.quietspace.common_service.model.response.ProfileResponse;
import com.jellybrains.quietspace.common_service.model.response.UserResponse;
import com.jellybrains.quietspace.user_service.entity.Profile;
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
