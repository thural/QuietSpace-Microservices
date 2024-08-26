package com.jellybrains.quietspace_backend_ms.feedservice.service.impls;

import com.jellybrains.quietspace.common_service.exception.CustomErrorException;
import com.jellybrains.quietspace.common_service.exception.UnauthorizedException;
import com.jellybrains.quietspace.common_service.model.request.CommentRequest;
import com.jellybrains.quietspace.common_service.model.response.CommentResponse;
import com.jellybrains.quietspace.common_service.service.UserService;
import com.jellybrains.quietspace_backend_ms.feedservice.entity.Comment;
import com.jellybrains.quietspace_backend_ms.feedservice.entity.Post;
import com.jellybrains.quietspace_backend_ms.feedservice.mapper.custom.CommentMapper;
import com.jellybrains.quietspace_backend_ms.feedservice.repository.CommentRepository;
import com.jellybrains.quietspace_backend_ms.feedservice.repository.PostRepository;
import com.jellybrains.quietspace_backend_ms.feedservice.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static com.jellybrains.quietspace.common_service.utils.PagingProvider.BY_CREATED_DATE_ASC;
import static com.jellybrains.quietspace.common_service.utils.PagingProvider.buildPageRequest;


@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public Page<CommentResponse> getCommentsByPostId(String postId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, BY_CREATED_DATE_ASC);
        return commentRepository.findAllByPostId(postId, pageRequest).map(commentMapper::commentEntityToResponse);
    }

    @Override
    public Page<CommentResponse> getCommentsByUserId(String userId, Integer pageNumber, Integer pageSize) {

        if (!userService.getAuthorizedUserId().equals(userId))
            throw new UnauthorizedException("user has no access to requested resource");

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        return commentRepository.findAllByUserId(userId, pageRequest).map(commentMapper::commentEntityToResponse);
    }

    @Override
    public CommentResponse createComment(CommentRequest comment) {
        String loggedUserId = userService.getAuthorizedUserId();

        Optional<Post> foundPost = postRepository.findById(comment.getPostId());

        if (!loggedUserId.equals(comment.getUserId()))
            throw new UnauthorizedException("resource does not belong to current user");
        if (foundPost.isEmpty())
            throw new EntityNotFoundException("post does not exist");

        Comment commentEntity = commentMapper.commentRequestToEntity(comment);
        return commentMapper.commentEntityToResponse(commentRepository.save(commentEntity));
    }

    @Override
    public Optional<CommentResponse> getCommentById(String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        CommentResponse commentResponse = commentMapper.commentEntityToResponse(comment);
        return Optional.of(commentResponse);
    }

    @Override
    public CommentResponse updateComment(String commentId, CommentRequest comment) {
        String loggedUserId = userService.getAuthorizedUserId();
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        if (existingComment.getUserId().equals(loggedUserId)) {
            existingComment.setText(comment.getText());
            Comment patchedComment = commentRepository.save(existingComment);
            return commentMapper.commentEntityToResponse(patchedComment);
        } else throw new CustomErrorException("comment author does not belong to current user");
    }

    @Override
    @Transactional
    public void deleteComment(String commentId) {
        String loggedUserId = userService.getAuthorizedUserId();

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        if (existingComment.getUserId().equals(loggedUserId)) {
            log.info("deleting comment {}", existingComment.getId());
            if (existingComment.getParentId() != null)
                commentRepository.deleteAllByParentId(existingComment.getParentId());
            commentRepository.deleteById(commentId);
        } else throw new CustomErrorException("comment author does not belong to current user");
    }

    @Override
    public Page<CommentResponse> getRepliesByParentId(String commentId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        return commentRepository.findAllByParentId(commentId, pageRequest).map(commentMapper::commentEntityToResponse);
    }

    @Override
    public CommentResponse patchComment(String commentId, CommentRequest comment) {
        String loggedUserId = userService.getAuthorizedUserId();

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        if (existingComment.getUserId().equals(loggedUserId)) {
            if (StringUtils.hasText(comment.getText())) existingComment.setText(comment.getText());
            Comment patchedComment = commentRepository.save(existingComment);
            return commentMapper.commentEntityToResponse(patchedComment);
        } else throw new CustomErrorException("comment author does not belong to current user");
    }

}
