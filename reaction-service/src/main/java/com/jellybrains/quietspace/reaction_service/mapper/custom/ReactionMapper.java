package com.jellybrains.quietspace.reaction_service.mapper.custom;

import com.jellybrains.quietspace.common_service.model.request.ReactionRequest;
import com.jellybrains.quietspace.common_service.model.response.ReactionResponse;
import com.jellybrains.quietspace.reaction_service.model.Reaction;
import com.jellybrains.quietspace.reaction_service.webclient.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ReactionMapper {

    private final UserService userService;

    public Reaction reactionRequestToEntity(ReactionRequest reaction) {
        return Reaction.builder()
                .userId(reaction.getUserId())
                .username(userService.getUsernameById(reaction.getUserId()))
                .contentId(reaction.getContentId())
                .contentType(reaction.getContentType())
                .reactionType(reaction.getReactionType())
                .build();
    }

    public ReactionResponse reactionEntityToResponse(Reaction reaction) {
        return ReactionResponse.builder()
                .id(reaction.getId())
                .contentId(reaction.getContentId())
                .reactionType(reaction.getReactionType())
                .userId(reaction.getUserId())
                .username(reaction.getUsername())
                .updateDate(reaction.getUpdatedDate())
                .build();
    }

}
