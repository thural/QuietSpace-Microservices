package com.jellybrains.quietspace_backend_ms.userservice.mapper.custom;

import com.jellybrains.quietspace_backend_ms.userservice.entity.Follow;
import com.jellybrains.quietspace_backend_ms.userservice.entity.User;
import com.jellybrains.quietspace_backend_ms.userservice.exception.UserNotFoundException;
import com.jellybrains.quietspace_backend_ms.userservice.model.request.FollowRequest;
import com.jellybrains.quietspace_backend_ms.userservice.model.response.FollowResponse;
import com.jellybrains.quietspace_backend_ms.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FollowMapper {
    private final UserRepository userRepository;
    public Follow followResponseToEntity(FollowRequest request){
        return Follow.builder()
                .following(getUserById(request.getFollowingId()))
                .follower(getUserById(request.getFollowerId()))
                .build();
    }

    public FollowResponse followEntityToResponse(Follow followEntity){
        return FollowResponse.builder()
                .followingId(followEntity.getFollowing().getId())
                .followerId(followEntity.getFollower().getId())
                .build();
    }

    private User getUserById(UUID userId){
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
}
