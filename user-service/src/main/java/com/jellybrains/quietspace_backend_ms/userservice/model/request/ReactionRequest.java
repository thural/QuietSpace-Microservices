package com.jellybrains.quietspace_backend_ms.userservice.model.request;

import com.jellybrains.quietspace_backend_ms.userservice.utils.enums.ContentType;
import com.jellybrains.quietspace_backend_ms.userservice.utils.enums.LikeType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReactionRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID contentId;

    @NotNull
    private ContentType contentType;

    @NotNull
    private LikeType likeType;

}