package com.jellybrains.quietspace_backend_ms.reaction_service.service.impls;

import com.jellybrains.quietspace_backend_ms.reaction_service.client.UserClient;
import com.jellybrains.quietspace_backend_ms.reaction_service.exception.UserNotFoundException;
import com.jellybrains.quietspace_backend_ms.reaction_service.mapper.custom.ReactionMapper;
import com.jellybrains.quietspace_backend_ms.reaction_service.model.Reaction;
import com.jellybrains.quietspace_backend_ms.reaction_service.model.request.ReactionRequest;
import com.jellybrains.quietspace_backend_ms.reaction_service.model.response.ReactionResponse;
import com.jellybrains.quietspace_backend_ms.reaction_service.model.response.UserResponse;
import com.jellybrains.quietspace_backend_ms.reaction_service.repository.ReactionRepository;
import com.jellybrains.quietspace_backend_ms.reaction_service.utils.enums.ContentType;
import com.jellybrains.quietspace_backend_ms.reaction_service.utils.enums.LikeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final ReactionMapper reactionMapper;
    private final UserClient userClient;

    @Override
    public void handleReaction(ReactionRequest reaction) {
        validateUserId(reaction.getUserId());
        Reaction foundReaction = reactionRepository
                .findByContentIdAndUserId(reaction.getContentId(), reaction.getUserId())
                .orElse(null);

        if (foundReaction == null) {
            reactionRepository.save(reactionMapper.reactionRequestToEntity(reaction));
        } else if (reaction.getLikeType().equals(foundReaction.getLikeType())) {
            reactionRepository.deleteById(foundReaction.getId());
        } else {
            foundReaction.setLikeType(reaction.getLikeType());
            reactionRepository.save(foundReaction);
        }
    }

    @Override
    public Optional<ReactionResponse> getUserReactionByContentId(UUID contentId) {
        validateUserId(reaction.getUserId());
        Optional<Reaction> userReaction = reactionRepository.findByContentIdAndUserId(contentId, user.getId());
        return userReaction.map(reactionMapper::reactionEntityToResponse);
    }

    @Override
    public List<ReactionResponse> getLikesByContentId(UUID contentId) {
        return reactionRepository.findAllByContentIdAndLikeType(contentId, LikeType.LIKE)
                .stream().map(reactionMapper::reactionEntityToResponse)
                .toList();
    }

    @Override
    public List<ReactionResponse> getDislikesByContentId(UUID contentId) {
        return reactionRepository.findAllByContentIdAndLikeType(contentId, LikeType.DISLIKE)
                .stream().map(reactionMapper::reactionEntityToResponse)
                .toList();
    }

    @Override
    public Integer getLikeCountByContentId(UUID contentId) {
        return reactionRepository.countByContentIdAndLikeType(contentId, LikeType.LIKE);
    }

    @Override
    public Integer getDislikeCountByContentId(UUID contentId) {
        return reactionRepository.countByContentIdAndLikeType(contentId, LikeType.DISLIKE);
    }

    @Override
    public List<ReactionResponse> getReactionsByContentId(UUID contentId, ContentType type) {
        return reactionRepository.findAllByContentIdAndContentType(contentId, type)
                .stream().map(reactionMapper::reactionEntityToResponse)
                .toList();
    }

    @Override
    public List<ReactionResponse> getReactionsByUserId(UUID userId, ContentType contentType) {
        return reactionRepository.findAllByUserIdAndContentType(userId, contentType)
                .stream().map(reactionMapper::reactionEntityToResponse)
                .toList();
    }

    private UserResponse getUserById(UUID memberId) {
        return userClient.getUserById(memberId)
                .orElseThrow(() -> new UserNotFoundException("user not found"));
    }


    private UserResponse getLoggedUser(){
        return userClient.getLoggedUser().orElseThrow(UserNotFoundException::new);
    }

}
