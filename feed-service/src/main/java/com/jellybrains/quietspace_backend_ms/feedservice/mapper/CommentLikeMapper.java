package com.jellybrains.quietspace_backend_ms.feedservice.mapper;

import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.CommentLikeResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.model.CommentLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CommentLikeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    CommentLike commentLikeDtoToEntity(CommentLikeResponse commentLikeResponse);

    @Mapping(target = "commentId", source = "comment.id")
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    CommentLikeResponse commentLikeEntityToDto(CommentLike commentLike);
}