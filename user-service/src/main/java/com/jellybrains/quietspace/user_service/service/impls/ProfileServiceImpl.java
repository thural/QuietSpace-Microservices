package com.jellybrains.quietspace.user_service.service.impls;

import com.jellybrains.quietspace.common_service.enums.StatusType;
import com.jellybrains.quietspace.common_service.model.request.CreateProfileRequest;
import com.jellybrains.quietspace.common_service.model.response.ProfileResponse;
import com.jellybrains.quietspace.common_service.model.response.UserResponse;
import com.jellybrains.quietspace.user_service.entity.Profile;
import com.jellybrains.quietspace.user_service.exception.CustomErrorException;
import com.jellybrains.quietspace.user_service.exception.UnauthorizedException;
import com.jellybrains.quietspace.user_service.exception.UserNotFoundException;
import com.jellybrains.quietspace.user_service.mapper.custom.ProfileMapper;
import com.jellybrains.quietspace.user_service.repository.ProfileRepository;
import com.jellybrains.quietspace.user_service.service.ProfileService;
import com.jellybrains.quietspace.user_service.webclient.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.jellybrains.quietspace.common_service.utils.PagingProvider.DEFAULT_SORT_OPTION;
import static com.jellybrains.quietspace.common_service.utils.PagingProvider.buildPageRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileMapper profileMapper;
    private final ProfileRepository profileRepository;
    private final UserService userService;

    @Override
    public Page<UserResponse> listUsersByQuery(String query, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, DEFAULT_SORT_OPTION);
        Page<Profile> profilePage;

        if (StringUtils.hasText(query)) {
            profilePage = profileRepository.findAllByQuery(query, pageRequest);
        } else {
            profilePage = profileRepository.findAll(pageRequest);
        }
        return profilePage.map(profileMapper::toUserResponse);
    }

    @Override
    public Optional<UserResponse> getSignedUserResponse() {
        UserResponse userResponse = profileMapper.toUserResponse(getUserProfile());
        return Optional.of(userResponse);
    }

    @Override
    public Profile getUserProfile() {
        String userId = userService.getAuthorizedUserId();
        log.info("authorized user id: {}", userId);
        return profileRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<UserResponse> getUsersFromIdList(List<String> userIds) {
        return userIds.stream()
                .map(userId -> profileRepository.findByUserId(userId)
                        .orElseThrow(UserNotFoundException::new))
                .map(profileMapper::toUserResponse).toList();
    }

    @Override
    public Optional<UserResponse> getUserResponseById(String id) {
        return profileRepository.findByUserId(id)
                .map(profileMapper::toUserResponse);
    }

    @Override
    public Optional<Profile> getProfileById(String userId) {
        return profileRepository.findById(userId);
    }

    @Override
    public void deleteProfileById(String userId) {
        Profile userProfile = getUserProfile();
        boolean hasAdminRole = isHasAdminRole(userProfile);

        if (!hasAdminRole && !userProfile.getId().equals(userId))
            throw new UnauthorizedException("profile denied access to delete the resource");

        profileRepository.deleteById(userId);
    }

    public ProfileResponse patchProfile(CreateProfileRequest request) {

        Profile profile = getUserProfile();
        boolean hasAdminRole = isHasAdminRole(profile);

        if (!hasAdminRole && !request.getEmail().equals(profile.getEmail()))
            throw new UnauthorizedException("signed profile has no access to requested resource");

        BeanUtils.copyProperties(profile, request);
        return profileMapper.toProfileResponse(profileRepository.save(profile));
    }

    private static boolean isHasAdminRole(Profile userProfile) {
        return true; // TODO get from authorization service using kafka or webclient
    }

    @Override
    public Page<UserResponse> listFollowings(Integer pageNumber, Integer pageSize) {
        Profile profile = getUserProfile();
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, DEFAULT_SORT_OPTION);
        Page<Profile> profilePage = profileRepository.findAllByUserIdIn(profile.getFollowingUserIds(), pageRequest);
        return profilePage.map(profileMapper::toUserResponse);
    }

    @Override
    public Page<UserResponse> listFollowers(Integer pageNumber, Integer pageSize) {
        Profile profile = getUserProfile();
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, DEFAULT_SORT_OPTION);
        Page<Profile> profilePage = profileRepository.findAllByUserIdIn(profile.getFollowerUserIds(), pageRequest);
        return profilePage.map(profileMapper::toUserResponse);
    }

    @Override
    @Transactional
    public void toggleFollow(String followedUserId) {
        Profile profile = getUserProfile();
        String profileUserId = profile.getUserId();

        if (profile.getUserId().equals(followedUserId))
            throw new CustomErrorException(HttpStatus.BAD_REQUEST,
                    "users can't unfollow themselves");

        Profile followedProfile = profileRepository.findByUserId(followedUserId)
                .orElseThrow(UserNotFoundException::new);

        if (profile.getFollowingUserIds().contains(followedUserId)) {
            profile.getFollowingUserIds().remove(followedUserId);
            followedProfile.getFollowerUserIds().remove(profileUserId);
        } else {
            profile.getFollowingUserIds().add(followedUserId);
            followedProfile.getFollowerUserIds().add(profileUserId);
        }
    }

    @Override
    @Transactional
    public void removeFollower(String followingUserId) {
        Profile profile = getUserProfile();
        String profileUserId = profile.getUserId();

        if (profile.getUserId().equals(followingUserId))
            throw new CustomErrorException(HttpStatus.BAD_REQUEST,
                    "users can't unfollow themselves");

        Profile followingProfile = profileRepository.findByUserId(followingUserId)
                .orElseThrow(UserNotFoundException::new);

        if (profile.getFollowerUserIds().contains(followingUserId)) {
            profile.getFollowerUserIds().remove(followingUserId);
            followingProfile.getFollowingUserIds().remove(profileUserId);
        } else throw new CustomErrorException("profile is not found in followers");
    }

    @Override
    public List<String> findConnectedFollowings() {
        Profile userProfile = getUserProfile();
        return userProfile.getFollowingUserIds().stream()
                .filter(userId -> {
                    Profile followingProfile = profileRepository.findByUserId(userId)
                            .orElseThrow(UserNotFoundException::new);
                    return followingProfile.getStatusType().equals(StatusType.ONLINE);
                }).toList();
    }

    @Override
    public Boolean validateUserInRequest(String userId) {
        return profileRepository.findByUserId(userId).isPresent();
    }

    @Override
    public Boolean validateUsersByIdList(List<String> userIds) {
        return userIds.stream()
                .allMatch(userId -> profileRepository.findByUserId(userId).isPresent());
    }

    @Override
    public UserResponse createProfile(CreateProfileRequest userRequest) {
        Profile profile = new Profile();
        BeanUtils.copyProperties(userRequest, profile);
        Profile createdProfile = profileRepository.save(profile);
        return profileMapper.toUserResponse(createdProfile);
    }

    @Override
    public void setOnlineStatus(String userEmail, StatusType type) {
        profileRepository.findByEmail(userEmail)
                .ifPresent((storedUser) -> {
                    storedUser.setStatusType(StatusType.OFFLINE);
                    profileRepository.save(storedUser);
                });
    }

}