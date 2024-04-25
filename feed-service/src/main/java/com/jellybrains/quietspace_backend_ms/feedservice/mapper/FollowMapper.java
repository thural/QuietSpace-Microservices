package com.jellybrains.quietspace_backend_ms.feedservice.mapper;

import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.FollowResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.model.Follow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FollowMapper {
    @Mapping(target = "id", ignore = true)
    Follow followResponseToEntity(FollowResponse followResponse);

    FollowResponse followEntityToResponse(Follow followEntity);
}
