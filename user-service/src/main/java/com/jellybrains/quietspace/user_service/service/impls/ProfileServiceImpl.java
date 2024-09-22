package com.jellybrains.quietspace.user_service.service.impls;

import com.jellybrains.quietspace.common_service.enums.EventType;
import com.jellybrains.quietspace.common_service.enums.NotificationType;
import com.jellybrains.quietspace.common_service.enums.RoleType;
import com.jellybrains.quietspace.common_service.enums.StatusType;
import com.jellybrains.quietspace.common_service.exception.CustomErrorException;
import com.jellybrains.quietspace.common_service.exception.UnauthorizedException;
import com.jellybrains.quietspace.common_service.exception.UserNotFoundException;
import com.jellybrains.quietspace.common_service.kafka.producer.NotificationProducer;
import com.jellybrains.quietspace.common_service.message.kafka.notification.NotificationEvent;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileUpdateEvent;
import com.jellybrains.quietspace.common_service.model.request.CreateProfileRequest;
import com.jellybrains.quietspace.common_service.model.response.UserResponse;
import com.jellybrains.quietspace.common_service.webclient.service.UserService;
import com.jellybrains.quietspace.common_service.websocket.model.UserRepresentation;
import com.jellybrains.quietspace.user_service.entity.Profile;
import com.jellybrains.quietspace.user_service.kafka.producer.ProfileProducer;
import com.jellybrains.quietspace.user_service.mapper.custom.ProfileMapper;
import com.jellybrains.quietspace.user_service.repository.impl.ProfileRepositoryImpl;
import com.jellybrains.quietspace.user_service.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.jellybrains.quietspace.common_service.utils.PagingProvider.DEFAULT_SORT_OPTION;
import static com.jellybrains.quietspace.common_service.utils.PagingProvider.buildPageRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileMapper profileMapper;
    private final ProfileProducer profileProducer;
    private final UserService userService;
    private final NotificationProducer notificationProducer;
    private final ProfileRepositoryImpl profileDao;

    @Override
    public Page<UserResponse> listUsersByQuery(String query, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, DEFAULT_SORT_OPTION);
        if (StringUtils.hasText(query)) return profileDao.findAllByQuery(query, pageRequest)
                .map(profileMapper::toUserResponse);
        return profileDao.findAll(pageRequest)
                .map(profileMapper::toUserResponse);
    }

    @Override
    public Optional<UserResponse> getSignedUserResponse() {
        UserResponse userResponse = profileMapper.toUserResponse(getUserProfile());
        return Optional.of(userResponse);
    }

    @Override
    public Profile getUserProfile() {
        String userId = userService.getAuthorizedUserId();
        return profileDao.findByUserId(userId).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<UserResponse> getUsersFromIdList(List<String> userIds) {
        return userIds.stream()
                .map(userId -> profileDao.findByUserId(userId)
                        .orElseThrow(UserNotFoundException::new))
                .map(profileMapper::toUserResponse).toList();
    }

    @Override
    public Optional<UserResponse> getUserResponseById(String id) {
        return profileDao.findByUserId(id)
                .map(profileMapper::toUserResponse);
    }

    @Override
    public Optional<Profile> getProfileById(String userId) {
        return profileDao.findById(userId);
    }

    @Override
    public void deleteProfileById(String userId) {
        Profile userProfile = getUserProfile();
        validateResourceAccess(userId, userProfile);
        profileDao.deleteById(userId);
    }

    public void requestProfileUpdate(UserRepresentation request) {
        Profile profile = getUserProfile();
        validateResourceAccess(request.getUserId(), profile);
        profileProducer.profileUpdate(ProfileUpdateEvent.builder()
                .type(EventType.PROFILE_UPDATE_REQUEST_EVENT)
                .eventBody(request).build()
        );
    }

    private static void validateResourceAccess(String userId, Profile profile) {
        boolean hasAdminRole = isHasAdminRole(profile);
        if (!hasAdminRole && !userId.equals(profile.getUserId()))
            throw new UnauthorizedException("signed user has no access to requested resource");
    }

    private static boolean isHasAdminRole(Profile userProfile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream().anyMatch(a -> a.getAuthority().equals(RoleType.ADMIN.toString()));
    }

    @Override
    public Page<UserResponse> listFollowings(Integer pageNumber, Integer pageSize) {
        Profile profile = getUserProfile();
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, DEFAULT_SORT_OPTION);
        Page<Profile> profilePage = profileDao.findAllByUserIdIn(profile.getFollowingUserIds(), pageRequest);
        return profilePage.map(profileMapper::toUserResponse);
    }

    @Override
    public Page<UserResponse> listFollowers(Integer pageNumber, Integer pageSize) {
        Profile profile = getUserProfile();
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, DEFAULT_SORT_OPTION);
        Page<Profile> profilePage = profileDao.findAllByUserIdIn(profile.getFollowerUserIds(), pageRequest);
        return profilePage.map(profileMapper::toUserResponse);
    }

    @Override
    @Transactional
    public void toggleFollow(String followedUserId) {
        Profile profile = getUserProfile();
        String profileUserId = profile.getUserId();
        if (profile.getUserId().equals(followedUserId))
            throw new CustomErrorException("users can't unfollow themselves");

        profileDao.findByUserId(followedUserId)
                .ifPresent(found -> {
                    if (profile.getFollowingUserIds().contains(followedUserId)) {
                        profile.getFollowingUserIds().remove(followedUserId);
                        found.getFollowerUserIds().remove(profileUserId);
                    } else {
                        profile.getFollowingUserIds().add(followedUserId);
                        found.getFollowerUserIds().add(profileUserId);
                        notificationProducer.sendNotification(NotificationEvent.builder()
                                .contentId(profile.getUserId())
                                .notificationType(NotificationType.FOLLOW_REQUEST)
                                .build()
                        );
                    }
                });
    }

    @Override
    @Transactional
    public void removeFollower(String followerUserId) {
        Profile profile = getUserProfile();
        String profileUserId = profile.getUserId();
        if (profile.getUserId().equals(followerUserId))
            throw new CustomErrorException("users can't unfollow themselves");
        Profile followerProfile = profileDao.findByUserId(followerUserId)
                .orElseThrow(UserNotFoundException::new);
        if (!profile.getFollowerUserIds().contains(followerUserId))
            throw new CustomErrorException("follower profile is not found in followers");
        profile.getFollowerUserIds().remove(followerUserId);
        followerProfile.getFollowingUserIds().remove(profileUserId);
    }

    @Override
    @Transactional
    public void removeFollowing(String followingUserId) {
        Profile profile = getUserProfile();
        String profileUserId = profile.getUserId();
        if (profile.getUserId().equals(followingUserId))
            throw new CustomErrorException("users can't unfollow themselves");
        Profile followingProfile = profileDao.findByUserId(followingUserId)
                .orElseThrow(UserNotFoundException::new);
        if (!profile.getFollowingUserIds().contains(followingUserId))
            throw new CustomErrorException("following profile is not found in followings");
        profile.getFollowingUserIds().remove(followingUserId);
        followingProfile.getFollowerUserIds().remove(profileUserId);
    }

    @Override
    public List<String> findConnectedFollowings() {
        Profile userProfile = getUserProfile();
        return userProfile.getFollowingUserIds().stream()
                .filter(userId -> {
                    Profile followingProfile = profileDao.findByUserId(userId)
                            .orElseThrow(UserNotFoundException::new);
                    return followingProfile.getStatusType().equals(StatusType.ONLINE);
                }).toList();
    }

    @Override
    public Boolean validateUserInRequest(String userId) {
        return profileDao.findByUserId(userId).isPresent();
    }

    @Override
    public Boolean validateUsersByIdList(List<String> userIds) {
        return userIds.stream()
                .allMatch(userId -> profileDao.findByUserId(userId).isPresent());
    }

    @Override
    public UserResponse createProfile(CreateProfileRequest userRequest) {
        Profile profile = new Profile();
        BeanUtils.copyProperties(userRequest, profile);
        Profile createdProfile = profileDao.save(profile);
        return profileMapper.toUserResponse(createdProfile);
    }

    @Override
    public void setOnlineStatus(String userEmail, StatusType type) {
        profileDao.findByEmail(userEmail)
                .ifPresent(storedUser -> {
                    storedUser.setStatusType(StatusType.OFFLINE);
                    profileDao.save(storedUser);
                });
    }

}