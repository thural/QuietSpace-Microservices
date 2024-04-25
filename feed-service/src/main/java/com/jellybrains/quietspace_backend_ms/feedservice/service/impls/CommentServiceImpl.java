package com.jellybrains.quietspace_backend_ms.feedservice.service.impls;

import com.jellybrains.quietspace_backend_ms.feedservice.dto.request.CommentRequest;
import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.CommentLikeResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.dto.response.CommentResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.mapper.CommentLikeMapper;
import com.jellybrains.quietspace_backend_ms.feedservice.mapper.CommentMapper;
import com.jellybrains.quietspace_backend_ms.feedservice.model.Comment;
import com.jellybrains.quietspace_backend_ms.feedservice.model.CommentLike;
import com.jellybrains.quietspace_backend_ms.feedservice.model.Post;
import com.jellybrains.quietspace_backend_ms.feedservice.repository.CommentLikeRepository;
import com.jellybrains.quietspace_backend_ms.feedservice.repository.CommentRepository;
import com.jellybrains.quietspace_backend_ms.feedservice.repository.PostRepository;
import com.jellybrains.quietspace_backend_ms.feedservice.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.jellybrains.quietspace_backend_ms.feedservice.utils.PagingProvider.buildCustomPageRequest;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentLikeMapper commentLikeMapper;
    // TODO: implement webflux to get users instead

    @Override
    public Page<CommentResponse> getCommentsByPost(UUID postId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildCustomPageRequest(pageNumber, pageSize);
        return commentRepository.findAllByPostId(postId, pageRequest).map(commentMapper::commentEntityToResponse);
    }

    @Override
    public CommentResponse createComment(CommentRequest comment) {
        UUID loggedUserId = getLoggedUserId();

        Optional<Post> foundPost = postRepository.findById(comment.getPostId());


        // TODO: check if logged user id matches comment author else throw error

        if (foundPost.isEmpty()) throw new EntityNotFoundException("post does not exist");

        Comment commentEntity = commentMapper.commentRequestToEntity(comment);
        commentEntity.setUserId(loggedUserId);
        commentEntity.setPost(foundPost.orElse(null));
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
    public void updateComment(UUID commentId, CommentRequest comment) {
        UUID loggedUserId = getLoggedUserId();

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        if (existingComment.getUserId().equals(loggedUserId)) {
            existingComment.setText(comment.getText());
            commentRepository.save(existingComment);
        } // else throw new AccessDeniedException("comment author does not belong to current user");

    }

    private UUID getLoggedUserId() {
        // TODO: get logged user using webflux
        return null;
    }

    @Override
    public void deleteComment(UUID commentId) {
        UUID loggedUserId = getLoggedUserId();

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        if (existingComment.getUserId().equals(loggedUserId)) {
            commentRepository.deleteById(commentId);
        } // else throw new AccessDeniedException("comment author does not belong to current user");
    }

    @Override
    public void patchComment(UUID commentId, CommentRequest comment) {
        UUID loggedUserId = getLoggedUserId();

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        if (existingComment.getUserId().equals(loggedUserId)) {
            if (StringUtils.hasText(comment.getText())) existingComment.setText(comment.getText());
            commentRepository.save(existingComment);
        } // else throw new AccessDeniedException("comment author does not belong to current user");

    }

    @Override
    public void toggleCommentLike(UUID commentId) {
        UUID loggedUserId = getLoggedUserId();

        boolean isLikeExists = commentLikeRepository
                .existsByCommentIdAndUserId(commentId, loggedUserId);

        if (isLikeExists) {
            CommentLike foundLike = commentLikeRepository
                    .findByCommentIdAndUserId(commentId, loggedUserId);
            commentLikeRepository.deleteById(foundLike.getId());
        } else {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(EntityNotFoundException::new);
            commentLikeRepository.save(CommentLike.builder().comment(comment).userId(loggedUserId).build());
        }
    }

    @Override
    public List<CommentLikeResponse> getLikesByCommentId(UUID commentId) {
        return commentLikeRepository.findAllByCommentId(commentId)
                .stream().map( commentLikeMapper::commentLikeEntityToDto)
                .toList();
    }

    @Override
    public List<CommentLikeResponse> getAllByUserId(UUID userId) {
        return commentLikeRepository.findAllByUserId(userId)
                .stream().map(commentLikeMapper::commentLikeEntityToDto)
                .toList();
    }

}
