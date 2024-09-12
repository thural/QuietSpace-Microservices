package com.jellybrains.quietspace.reaction_service.service.impls;

import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.NotificationType;
import com.jellybrains.quietspace.common_service.enums.ReactionType;
import com.jellybrains.quietspace.common_service.kafka.producer.NotificationProducer;
import com.jellybrains.quietspace.common_service.message.kafka.notification.NotificationEvent;
import com.jellybrains.quietspace.common_service.webclient.service.UserService;
import com.jellybrains.quietspace.reaction_service.model.Reaction;
import com.jellybrains.quietspace.reaction_service.repository.ReactionRepository;
import com.jellybrains.quietspace.reaction_service.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.jellybrains.quietspace.common_service.utils.PagingProvider.DEFAULT_SORT_OPTION;
import static com.jellybrains.quietspace.common_service.utils.PagingProvider.buildPageRequest;


@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final NotificationProducer producer;
    private final UserService userService;

    @Override
    public Mono<Void> handleReaction(Reaction request) {
        String userId = userService.getAuthorizedUserId();

        return reactionRepository
                .findByContentIdAndUserId(request.getContentId(), userId)
                .switchIfEmpty(reactionRepository.save(request))
                .doOnNext(reaction -> {
                    if (request.getReactionType().equals(reaction.getReactionType())) {
                        reactionRepository.deleteById(reaction.getId());
                    }
                    reaction.setReactionType(request.getReactionType());
                })
                .doOnNext(reaction -> {
                    NotificationType type = getNotificationType(reaction.getContentType());
                    producer.sendNotification(NotificationEvent.builder()
                            .contentType(reaction.getContentType())
                            .contentId(reaction.getContentId())
                            .notificationType(type).build());
                }).then();
    }

    public NotificationType getNotificationType(ContentType type) {
        return switch (type) {
            case COMMENT -> NotificationType.COMMENT_REACTION;
            case POST -> NotificationType.POST_REACTION;
            default -> null;
        };
    }

    @Override
    public Mono<Reaction> getUserReactionByContentId(String contentId) {
        String userId = userService.getAuthorizedUserId();
        return reactionRepository.findByContentIdAndUserId(contentId, userId);
    }

    @Override
    public Flux<Reaction> getReactionsByContentIdAndType(String contentId, ReactionType reactionType, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, DEFAULT_SORT_OPTION);
        return reactionRepository.findAllByContentIdAndReactionType(contentId, ReactionType.LIKE, pageRequest);
    }

    @Override
    public Mono<Integer> countByContentIdAndType(String contentId, ReactionType reactionType) {
        return reactionRepository.countByContentIdAndReactionType(contentId, ReactionType.LIKE);
    }

    @Override
    public Flux<Reaction> getAllByContentIdAndContentType(String contentId, ContentType type, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, DEFAULT_SORT_OPTION);
        return reactionRepository.findAllByContentIdAndContentType(contentId, type, pageRequest);
    }

    @Override
    public Flux<Reaction> getReactionsByUserIdAndContentType(String userId, ContentType contentType, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, DEFAULT_SORT_OPTION);
        return reactionRepository.findAllByUserIdAndContentType(userId, contentType, pageRequest);
    }

}
