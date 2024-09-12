package com.jellybrains.quietspace.feed_service.service.impls;

import com.jellybrains.quietspace.common_service.enums.NotificationType;
import com.jellybrains.quietspace.common_service.kafka.producer.NotificationProducer;
import com.jellybrains.quietspace.common_service.message.kafka.notification.NotificationEvent;
import com.jellybrains.quietspace.common_service.model.request.CommentRequest;
import com.jellybrains.quietspace.common_service.model.response.CommentResponse;
import com.jellybrains.quietspace.common_service.webclient.service.UserService;
import com.jellybrains.quietspace.feed_service.entity.Comment;
import com.jellybrains.quietspace.feed_service.exception.CustomErrorException;
import com.jellybrains.quietspace.feed_service.mapper.custom.CommentMapper;
import com.jellybrains.quietspace.feed_service.repository.CommentRepository;
import com.jellybrains.quietspace.feed_service.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.jellybrains.quietspace.common_service.utils.PagingProvider.BY_CREATED_DATE_ASC;
import static com.jellybrains.quietspace.common_service.utils.PagingProvider.buildPageRequest;


@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final NotificationProducer notificationProducer;

    @Override
    public Flux<CommentResponse> getCommentsByPostId(String postId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, BY_CREATED_DATE_ASC);
        return commentRepository.findAllByPostId(postId, pageRequest).map(commentMapper::toResponse);
    }

    @Override
    public Flux<CommentResponse> getCommentsByUser(Integer pageNumber, Integer pageSize) {
        String userId = userService.getAuthorizedUserId();
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        return commentRepository.findAllByUserId(userId, pageRequest).map(commentMapper::toResponse);
    }

    @Override
    public Mono<CommentResponse> createComment(CommentRequest request) {
        Comment entity = commentMapper.toEntity(request);
        notificationProducer.sendNotification(NotificationEvent.builder()
                .contentId(request.getPostId())
                .notificationType(NotificationType.COMMENT).build());
        return commentRepository.save(entity).map(commentMapper::toResponse);
    }

    @Override
    public Mono<CommentResponse> getCommentById(String commentId) {
        return commentRepository.findById(commentId)
                .switchIfEmpty(Mono.error(EntityNotFoundException::new)).map(commentMapper::toResponse);
    }

    @Override
    public Flux<CommentResponse> getRepliesByParentId(String commentId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, null);
        return commentRepository.findAllByParentId(commentId, pageRequest).map(commentMapper::toResponse);
    }

    @Override
    @Transactional
    public Mono<Void> deleteComment(String commentId) {
        return commentRepository.findById(commentId)
                .switchIfEmpty(Mono.error(EntityNotFoundException::new))
                .doOnNext(comment -> validateOwnership(comment.getUserId()))
                .doOnNext(comment -> {
                    if (Objects.nonNull(comment.getParentId()))
                        commentRepository.deleteAllByParentId(comment.getParentId())
                                .subscribe();
                })
                .doOnNext(c -> commentRepository.deleteById(commentId)).then();
    }

    @Override
    public Mono<CommentResponse> patchComment(CommentRequest request) {
        return commentRepository.findById(request.getCommentId())
                .switchIfEmpty(Mono.error(EntityNotFoundException::new))
                .doOnNext(c -> validateOwnership(c.getUserId()))
                .doOnNext(c -> {
                    if (StringUtils.hasText(request.getText())) c.setText(request.getText());
                })
                .flatMap(commentRepository::save).map(commentMapper::toResponse);
    }

    private void validateOwnership(String userId) {
        if (!userId.equals(userService.getAuthorizedUserId()))
            throw new CustomErrorException("current user has no ownership over the requested resource");
    }

}
