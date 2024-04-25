package com.jellybrains.quietspace_backend_ms.feedservice.service.impls;

import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.FollowResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.mapper.FollowMapper;
import com.jellybrains.quietspace_backend_ms.feedservice.model.Follow;
import com.jellybrains.quietspace_backend_ms.feedservice.repository.FollowRepository;
import com.jellybrains.quietspace_backend_ms.feedservice.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.jellybrains.quietspace_backend_ms.feedservice.utils.PagingProvider.buildCustomPageRequest;


@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowMapper followMapper;
    private final FollowRepository followRepository;
    // TODO: implement webflux method to get users instead


    private UUID getLoggedUserId() {
        // TODO: get logged user using webflux
        return null;
    }

    private void checkUserValidity(UUID userId) {
        // TODO: check if user with id exists else throw error
    }


    @Override
    public Page<FollowResponse> listFollowings(Integer pageNumber, Integer pageSize) {
        UUID loggedUserId = getLoggedUserId();

        PageRequest pageRequest = buildCustomPageRequest(pageNumber, pageSize);
        Page<Follow> userPage = followRepository.findAllByFollowingId(loggedUserId, pageRequest);

        return userPage.map(followMapper::followEntityToResponse);
    }


    @Override
    public Page<FollowResponse> listFollowers(Integer pageNumber, Integer pageSize) {
        UUID loggedUserId = getLoggedUserId();

        PageRequest pageRequest = buildCustomPageRequest(pageNumber, pageSize);
        Page<Follow> userPage = followRepository.findAllByFollowerId(loggedUserId, pageRequest);

        return userPage.map(followMapper::followEntityToResponse);
    }

    @Override
    public void toggleFollow(UUID followedUserId) {
        UUID loggedUserId = getLoggedUserId();

        if (followRepository.existsByFollowerIdAndFollowingId(loggedUserId, followedUserId)) {
            followRepository.deleteByFollowerIdAndFollowingId(loggedUserId, followedUserId);
            return;
        }

        checkUserValidity(loggedUserId);
        checkUserValidity(followedUserId);


        Follow newFollowEntity = Follow.builder()
                    .followerId(loggedUserId)
                    .followingId(followedUserId)
                    .build();
        followRepository.save(newFollowEntity);
    }


}
