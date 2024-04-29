package com.jellybrains.quietspace_backend_ms.feedservice.mapper;

import com.jellybrains.quietspace_backend_ms.feedservice.dto.request.CommentRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.CommentResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    Comment commentRequestToEntity(CommentRequest commentRequest);

    @Mapping(target = "postId", source = "post.id")
    CommentResponse commentEntityToResponse(Comment comment);
}
