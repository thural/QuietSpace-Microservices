package com.jellybrains.quietspace_backend_ms.feedservice.mapper;

import com.jellybrains.quietspace_backend_ms.feedservice.dto.request.PostRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.PostResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PostMapper {
    @Mapping(target = "id", ignore = true)
    Post postRequestToEntity(PostRequest postRequest);

    @Mapping(target = "username", source ="user.username")
    @Mapping(target = "userId", source ="user.id")
    PostResponse postEntityToResponse(Post post);
}
