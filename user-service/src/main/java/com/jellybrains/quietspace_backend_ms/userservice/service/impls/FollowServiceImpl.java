package com.jellybrains.quietspace_backend_ms.userservice.service.impls;

import com.jellybrains.quietspace_backend_ms.userservice.entity.Follow;
import com.jellybrains.quietspace_backend_ms.userservice.entity.User;
import com.jellybrains.quietspace_backend_ms.userservice.exception.UserNotFoundException;
import com.jellybrains.quietspace_backend_ms.userservice.mapper.custom.FollowMapper;
import com.jellybrains.quietspace_backend_ms.userservice.model.response.FollowResponse;
import com.jellybrains.quietspace_backend_ms.userservice.repository.FollowRepository;
import com.jellybrains.quietspace_backend_ms.userservice.repository.UserRepository;
import com.jellybrains.quietspace_backend_ms.userservice.service.FollowService;
import com.jellybrains.quietspace_backend_ms.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.jellybrains.quietspace_backend_ms.userservice.utils.PagingProvider.buildPageRequest;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowMapper followMapper;
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public Page<FollowResponse> listFollowings(Integer pageNumber, Integer pageSize) {
        User user = userService.getLoggedUser();

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        Page<Follow> userPage = followRepository.findAllByFollowingId(user.getId(), pageRequest);

        return userPage.map(followMapper::followEntityToResponse);
    }

    @Override
    public Page<FollowResponse> listFollowers(Integer pageNumber, Integer pageSize) {
        User user = userService.getLoggedUser();

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        Page<Follow> userPage = followRepository.findAllByFollowerId(user.getId(), pageRequest);

        return userPage.map(followMapper::followEntityToResponse);
    }

    @Override
    @Transactional
    public void toggleFollow(UUID followedUserId) {
        User user = userService.getLoggedUser();

        if (followRepository.existsByFollowerIdAndFollowingId(user.getId(), followedUserId)) {
            followRepository.deleteByFollowerIdAndFollowingId(user.getId(), followedUserId);
            return;
        }

        User followingUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new UserNotFoundException("user not found with id: " + user.getId()));

        User followedUser = userRepository.findById(followedUserId)
                    .orElseThrow(() -> new UserNotFoundException("user not found with id: " + followedUserId));

        System.out.println("FOLLOWED user id: " + followedUser.getId());
        System.out.println("FOLLOWING user id: " + followingUser.getId());

        Follow newFollowEntity = Follow.builder()
                    .follower(followingUser)
                    .following(followedUser)
                    .build();
        followRepository.save(newFollowEntity);
    }

}
