package com.jellybrains.quietspace_backend_ms.userservice.mapper;

import com.jellybrains.quietspace_backend_ms.userservice.entity.Follow;
import com.jellybrains.quietspace_backend_ms.userservice.model.response.FollowResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FollowMapper {
    @Mapping(target = "id", ignore = true)
    Follow followResponseToEntity(FollowResponse followResponse);

    @Mapping(target = "followingId", source = "following.id")
    @Mapping(target = "followerId", source = "follower.id")
    FollowResponse followEntityToResponse(Follow followEntity);
}
