package com.jellybrains.quietspace_backend_ms.feedservice.mapper.custom;

import com.jellybrains.quietspace_backend_ms.feedservice.client.ReactionClient;
import com.jellybrains.quietspace_backend_ms.feedservice.client.UserClient;
import com.jellybrains.quietspace_backend_ms.feedservice.entity.Comment;
import com.jellybrains.quietspace_backend_ms.feedservice.entity.Post;
import com.jellybrains.quietspace_backend_ms.feedservice.model.request.CommentRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.model.response.CommentResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.model.response.ReactionResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.repository.CommentRepository;
import com.jellybrains.quietspace_backend_ms.feedservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReactionClient reactionClient;
    private final UserClient userClient;


    public Comment commentRequestToEntity(CommentRequest comment){
        return Comment.builder()
                .parentId(comment.getParentId())
                .text(comment.getText())
                .userId(comment.getUserId())
                .post(getPostById(comment.getPostId()))
                .build();
    }

    public CommentResponse commentEntityToResponse(Comment comment){
        return CommentResponse.builder()
                .id(comment.getId())
                .parentId(comment.getParentId())
                .postId(comment.getPost().getId())
                .userId(comment.getUserId())
                .username(getUserNameById(comment.getUserId()))
                .text(comment.getText())
                .userReaction(getUserReaction(comment.getId()))
                .createDate(comment.getCreateDate())
                .updateDate(comment.getUpdateDate())
                .likeCount(getLikeCount(comment.getId()))
                .replyCount(getReplyCount(comment.getId(), comment.getPost()))
                .build();
    }

    private Post getPostById(UUID postId){
        return postRepository.findById(postId).orElse(null);
    }

    private String getUserNameById(UUID userId){
        return userClient.getUserById(userId)
                .getUsername(); // TODO: get user from webclient
    }

    private Integer getReplyCount(UUID parentId, Post post){
        return commentRepository.countByParentIdAndPost(parentId, post);
    }

    private Integer getLikeCount(UUID commentId){
        return reactionClient.getLikeCountByContentId(commentId); // TODO: get reaction from webclient, add likeType to request
    }

    private ReactionResponse getUserReaction(UUID commentId){
        return reactionClient.getUserReactionByContentId(commentId); // TODO: get reaction from webclient
    }

}
