package com.jellybrains.quietspace_backend_ms.feedservice.mapper;

import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.PostLikeResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.model.PostLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PostLikeMapper {
    @Mapping(target = "id", ignore = true)
    PostLike postLikeDtoToEntity(PostLikeResponse postLikeResponse);

    PostLikeResponse postLikeEntityToDto(PostLike postLike);
}