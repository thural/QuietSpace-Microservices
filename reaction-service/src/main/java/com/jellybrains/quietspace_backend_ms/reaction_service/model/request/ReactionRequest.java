package com.jellybrains.quietspace_backend_ms.reaction_service.model.request;

import com.jellybrains.quietspace_backend_ms.reaction_service.utils.enums.ContentType;
import com.jellybrains.quietspace_backend_ms.reaction_service.utils.enums.LikeType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReactionRequest {

    @NotNull(message = "reaction user id can not be null")
    private String userId;

    @NotNull(message = "reaction content id can not be null")
    private String contentId;

    @NotNull(message = "reaction content type can not be null")
    private ContentType contentType;

    @NotNull(message = "reaction type can not be null")
    private LikeType likeType;

}