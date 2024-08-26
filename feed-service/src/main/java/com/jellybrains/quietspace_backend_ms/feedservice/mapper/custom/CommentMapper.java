package com.jellybrains.quietspace_backend_ms.feedservice.mapper.custom;

import com.jellybrains.quietspace.common_service.model.request.CommentRequest;
import com.jellybrains.quietspace.common_service.model.response.CommentResponse;
import com.jellybrains.quietspace.common_service.service.ReactionService;
import com.jellybrains.quietspace.common_service.service.UserService;
import com.jellybrains.quietspace_backend_ms.feedservice.entity.Comment;
import com.jellybrains.quietspace_backend_ms.feedservice.entity.Post;
import com.jellybrains.quietspace_backend_ms.feedservice.repository.CommentRepository;
import com.jellybrains.quietspace_backend_ms.feedservice.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ReactionService reactionService;


    public Comment commentRequestToEntity(CommentRequest comment) {
        return Comment.builder()
                .parentId(comment.getParentId())
                .text(comment.getText())
                .userId(comment.getUserId())
                .post(getPostById(comment.getPostId()))
                .build();
    }

    public CommentResponse commentEntityToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .parentId(comment.getParentId())
                .postId(comment.getPost().getId())
                .userId(comment.getUserId())
                .username(userService.getUsernameById(comment.getUserId()))
                .text(comment.getText())
                .userReaction(reactionService.getUserReactionByContentId(comment.getId()))
                .createDate(comment.getCreateDate())
                .updateDate(comment.getUpdateDate())
                .likeCount(reactionService.getLikeCount(comment.getId()))
                .replyCount(getReplyCount(comment.getId(), comment.getPost()))
                .build();
    }

    private Post getPostById(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);
    }

    private Integer getReplyCount(String parentId, Post post) {
        return commentRepository.countByParentIdAndPost(parentId, post);
    }

}
