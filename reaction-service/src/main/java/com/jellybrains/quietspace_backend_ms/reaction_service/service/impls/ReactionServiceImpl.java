package com.jellybrains.quietspace_backend_ms.reaction_service.service.impls;

import com.jellybrains.quietspace_backend_ms.reaction_service.client.UserClient;
import com.jellybrains.quietspace_backend_ms.reaction_service.exception.UserNotFoundException;
import com.jellybrains.quietspace_backend_ms.reaction_service.mapper.custom.ReactionMapper;
import com.jellybrains.quietspace_backend_ms.reaction_service.model.Reaction;
import com.jellybrains.quietspace_backend_ms.reaction_service.model.request.ReactionRequest;
import com.jellybrains.quietspace_backend_ms.reaction_service.model.response.ReactionResponse;
import com.jellybrains.quietspace_backend_ms.reaction_service.model.response.UserResponse;
import com.jellybrains.quietspace_backend_ms.reaction_service.repository.ReactionRepository;
import com.jellybrains.quietspace_backend_ms.reaction_service.service.ReactionService;
import com.jellybrains.quietspace_backend_ms.reaction_service.utils.enums.ContentType;
import com.jellybrains.quietspace_backend_ms.reaction_service.utils.enums.LikeType;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.jellybrains.quietspace_backend_ms.reaction_service.utils.PagingProvider.BY_CREATED_DATE_ASC;
import static com.jellybrains.quietspace_backend_ms.reaction_service.utils.PagingProvider.buildPageRequest;

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
    public Optional<ReactionResponse> getUserReactionByContentId(String contentId) {
        UserResponse user = getLoggedUser();
        Optional<Reaction> userReaction = reactionRepository.findByContentIdAndUserId(contentId, user.getId());
        return userReaction.map(reactionMapper::reactionEntityToResponse);
    }

    @Override
    public Page<ReactionResponse> getLikesByContentId(String contentId,Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, BY_CREATED_DATE_ASC);
        return reactionRepository.findAllByContentIdAndLikeType(contentId, LikeType.LIKE, pageRequest )
                .map(reactionMapper::reactionEntityToResponse);
    }

    @Override
    public Page<ReactionResponse> getDislikesByContentId(String contentId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, BY_CREATED_DATE_ASC);
        return reactionRepository.findAllByContentIdAndLikeType(contentId, LikeType.DISLIKE, pageRequest)
                .map(reactionMapper::reactionEntityToResponse);
    }

    @Override
    public Integer getLikeCountByContentId(String contentId) {
        return reactionRepository.countByContentIdAndLikeType(contentId, LikeType.LIKE);
    }

    @Override
    public Integer getDislikeCountByContentId(String contentId) {
        return reactionRepository.countByContentIdAndLikeType(contentId, LikeType.DISLIKE);
    }

    @Override
    public Page<ReactionResponse> getReactionsByContentId(String contentId, ContentType type, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, BY_CREATED_DATE_ASC);
        return reactionRepository.findAllByContentIdAndContentType(contentId, type, pageRequest)
                .map(reactionMapper::reactionEntityToResponse);
    }

    @Override
    public Page<ReactionResponse> getReactionsByUserId(String userId, ContentType contentType, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, BY_CREATED_DATE_ASC);
        return reactionRepository.findAllByUserIdAndContentType(userId, contentType, pageRequest)
                .map(reactionMapper::reactionEntityToResponse);
    }

    @Override
    public Page<ReactionResponse> getAllReactionsByUserId(String userId, ContentType contentType, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, BY_CREATED_DATE_ASC);
        return reactionRepository.findAllByUserId(userId, pageRequest).map(reactionMapper::reactionEntityToResponse);
    }

    private UserResponse getUserById(String memberId) {
        return userClient.getUserById(memberId)
                .orElseThrow(() -> new UserNotFoundException("user not found"));
    }

    private UserResponse getLoggedUser(){
        return userClient.getLoggedUser()
                .orElseThrow(() -> new UserNotFoundException("user not found"));
    }

    private void validateUserId(String userId) {
        if (!userClient.validateUserId(userId))
            throw new BadRequestException("user mismatch with requested chat member");
    }

}
