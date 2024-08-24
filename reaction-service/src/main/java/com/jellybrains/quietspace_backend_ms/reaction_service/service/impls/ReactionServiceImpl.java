package com.jellybrains.quietspace_backend_ms.reaction_service.service.impls;


import com.jellybrains.quietspace_backend_ms.reaction_service.common.service.UserService;
import com.jellybrains.quietspace_backend_ms.reaction_service.common.enums.ContentType;
import com.jellybrains.quietspace_backend_ms.reaction_service.common.enums.ReactionType;
import com.jellybrains.quietspace_backend_ms.reaction_service.mapper.custom.ReactionMapper;
import com.jellybrains.quietspace_backend_ms.reaction_service.model.Reaction;
import com.jellybrains.quietspace_backend_ms.reaction_service.repository.ReactionRepository;
import com.jellybrains.quietspace_backend_ms.reaction_service.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.jellybrains.quietspace_backend_ms.reaction_service.common.utils.PagingProvider.DEFAULT_SORT_OPTION;
import static com.jellybrains.quietspace_backend_ms.reaction_service.common.utils.PagingProvider.buildPageRequest;


@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final ReactionMapper reactionMapper;
    private final UserService userService;

    @Override
    public void handleReaction(Reaction reaction) {
        String userId = userService.getAuthorizedUserId();
        Reaction foundReaction = reactionRepository
                .findByContentIdAndUserId(reaction.getContentId(), userId)
                .orElse(null);

        if (foundReaction == null) {
            reactionRepository.save(reaction);
        } else if (reaction.getReactionType().equals(foundReaction.getReactionType())) {
            reactionRepository.deleteById(foundReaction.getId());
        } else {
            foundReaction.setReactionType(reaction.getReactionType());
            reactionRepository.save(foundReaction);
        }
    }

    @Override
    public Optional<Reaction> getUserReactionByContentId(String contentId) {
        String userId = userService.getAuthorizedUserId();
        return reactionRepository.findByContentIdAndUserId(contentId, userId);
    }

    @Override
    public Page<Reaction> getReactionsByContentIdAndReactionType(String contentId, ReactionType reactionType, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, DEFAULT_SORT_OPTION);
        return reactionRepository.findAllByContentIdAndReactionType(contentId, ReactionType.LIKE, pageRequest);
    }

    @Override
    public Integer countByContentIdAndReactionType(String contentId, ReactionType reactionType) {
        return reactionRepository.countByContentIdAndReactionType(contentId, ReactionType.LIKE);
    }

    @Override
    public Page<Reaction> getReactionsByContentIdAndContentType(String contentId, ContentType type, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, DEFAULT_SORT_OPTION);
        return reactionRepository.findAllByContentIdAndContentType(contentId, type, pageRequest);
    }

    @Override
    public Page<Reaction> getReactionsByUserIdAndContentType(String userId, ContentType contentType, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, DEFAULT_SORT_OPTION);
        return reactionRepository.findAllByUserIdAndContentType(userId, contentType, pageRequest);
    }

}
