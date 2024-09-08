package com.jellybrains.quietspace.feed_service.service.impls;

import com.jellybrains.quietspace.common_service.model.request.CommentRequest;
import com.jellybrains.quietspace.common_service.model.response.CommentResponse;
import com.jellybrains.quietspace.common_service.webclient.service.UserService;
import com.jellybrains.quietspace.feed_service.entity.Comment;
import com.jellybrains.quietspace.feed_service.entity.Post;
import com.jellybrains.quietspace.feed_service.exception.CustomErrorException;
import com.jellybrains.quietspace.feed_service.mapper.custom.CommentMapper;
import com.jellybrains.quietspace.feed_service.repository.CommentRepository;
import com.jellybrains.quietspace.feed_service.repository.PostRepository;
import com.jellybrains.quietspace.feed_service.service.CommentService;
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
    public Page<CommentResponse> getCommentsByUser(Integer pageNumber, Integer pageSize) {
        String userId = userService.getAuthorizedUserId();
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        return commentRepository.findAllByUserId(userId, pageRequest).map(commentMapper::commentEntityToResponse);
    }

    @Override
    public CommentResponse createComment(CommentRequest comment) {
        Optional<Post> foundPost = postRepository.findById(comment.getPostId());
        if (foundPost.isEmpty()) throw new EntityNotFoundException("post does not exist");
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
    public Page<CommentResponse> getRepliesByParentId(String commentId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        return commentRepository.findAllByParentId(commentId, pageRequest).map(commentMapper::commentEntityToResponse);
    }

    @Override
    @Transactional
    public void deleteComment(String commentId) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);
        validateOwnership(existingComment.getUserId());
        if (existingComment.getParentId() != null)
            commentRepository.deleteAllByParentId(existingComment.getParentId());
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentResponse patchComment(CommentRequest comment) {
        Comment existingComment = commentRepository.findById(comment.getCommentId())
                .orElseThrow(EntityNotFoundException::new);
        validateOwnership(existingComment.getUserId());
        if (StringUtils.hasText(comment.getText())) existingComment.setText(comment.getText());
        return commentMapper.commentEntityToResponse(commentRepository.save(existingComment));
    }

    private void validateOwnership(String userId) {
        if (!userId.equals(userService.getAuthorizedUserId()))
            throw new CustomErrorException("requested resource does not belong to user in request");
    }

}
