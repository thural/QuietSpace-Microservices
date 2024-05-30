package com.jellybrains.quietspace_backend_ms.feedservice.service.impls;

import com.jellybrains.quietspace_backend_ms.feedservice.entity.Comment;
import com.jellybrains.quietspace_backend_ms.feedservice.entity.Post;
import com.jellybrains.quietspace_backend_ms.feedservice.mapper.custom.CommentMapper;
import com.jellybrains.quietspace_backend_ms.feedservice.model.request.CommentRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.model.response.CommentResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.repository.CommentRepository;
import com.jellybrains.quietspace_backend_ms.feedservice.repository.PostRepository;
import com.jellybrains.quietspace_backend_ms.feedservice.client.UserClient;
import com.jellybrains.quietspace_backend_ms.feedservice.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

import static com.jellybrains.quietspace_backend_ms.feedservice.utils.PagingProvider.BY_CREATED_DATE_ASC;
import static com.jellybrains.quietspace_backend_ms.feedservice.utils.PagingProvider.buildPageRequest;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final UserClient userClient;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public Page<CommentResponse> getCommentsByPost(UUID postId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, BY_CREATED_DATE_ASC);
        return commentRepository.findAllByPostId(postId, pageRequest).map(commentMapper::commentEntityToResponse);
    }

    @Override
    public Page<CommentResponse> getCommentsByUser(UUID userId, Integer pageNumber, Integer pageSize) {

        if (!userClient.validateUserId(userId))
            throw new BadRequestException("user has no access to requested resource");

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        return commentRepository.findAllByUserId(userId, pageRequest).map(commentMapper::commentEntityToResponse);
    }

    @Override
    public CommentResponse createComment(CommentRequest comment) {

        Optional<Post> foundPost = postRepository.findById(comment.getPostId());

        if (!userClient.validateUserId(comment.getUserId()))
            throw new BadRequestException("user id mismatch in request");
        if (foundPost.isEmpty())
            throw new EntityNotFoundException("post does not exist");

        Comment commentEntity = commentMapper.commentRequestToEntity(comment);
        return commentMapper.commentEntityToResponse(commentRepository.save(commentEntity));
    }

    @Override
    public Optional<CommentResponse> getCommentById(UUID commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        CommentResponse commentResponse = commentMapper.commentEntityToResponse(comment);
        return Optional.of(commentResponse);
    }

    @Override
    public CommentResponse updateComment(UUID commentId, CommentRequest comment) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        if (userClient.validateUserId(existingComment.getUserId())) {
            existingComment.setText(comment.getText());
            Comment patchedComment = commentRepository.save(existingComment);
            return commentMapper.commentEntityToResponse(patchedComment);
        } else throw new BadRequestException("user is not authorized to update this comment");
    }

    @Override
    @Transactional
    public void deleteComment(UUID commentId) {

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        if (!userClient.validateUserId(existingComment.getUserId())) {
            commentRepository.deleteAllByParentId(commentId);
            commentRepository.deleteById(commentId);
        } else throw new BadRequestException("user is not allowed to delete this comment");
    }

    @Override
    public Page<CommentResponse> getRepliesByParentId(UUID commentId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        return commentRepository.findAllByParentId(commentId, pageRequest).map(commentMapper::commentEntityToResponse);
    }

    @Override
    public CommentResponse patchComment(UUID commentId, CommentRequest comment) {

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        if (userClient.validateUserId(existingComment.getUserId())) {
            if (StringUtils.hasText(comment.getText())) existingComment.setText(comment.getText());
            Comment patchedComment = commentRepository.save(existingComment);
            return commentMapper.commentEntityToResponse(patchedComment);
        } else throw new BadRequestException("user is not allowed to update this comment");
    }

}
