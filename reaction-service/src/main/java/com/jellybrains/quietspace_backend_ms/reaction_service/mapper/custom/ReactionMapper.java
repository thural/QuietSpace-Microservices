package com.jellybrains.quietspace_backend_ms.reaction_service.mapper.custom;

import com.jellybrains.quietspace_backend_ms.reaction_service.model.Reaction;
import com.jellybrains.quietspace_backend_ms.reaction_service.model.request.ReactionRequest;
import com.jellybrains.quietspace_backend_ms.reaction_service.model.response.ReactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReactionMapper {

    // TODO: implement webclient for users

    public Reaction reactionRequestToEntity(ReactionRequest reaction){
        return Reaction.builder()
                .userId(reaction.getUserId())
                .contentId(reaction.getContentId())
                .contentType(reaction.getContentType())
                .likeType(reaction.getLikeType())
                .build();
    }

    public ReactionResponse reactionEntityToResponse(Reaction reaction){
        return ReactionResponse.builder()
                .id(reaction.getId())
                .contentId(reaction.getContentId())
                .likeType(reaction.getLikeType())
                .userId(reaction.getUserId())
                .updateDate(reaction.getUpdateDDate())
                .build();
    }

    String getUserNameById(UUID userId){
        return null;
    }

}
