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

    @NotNull(message = "user id can not be null")
    private UUID userId;

    @NotNull(message = "content id can not be null")
    private UUID contentId;

    @NotNull(message = "content type can not be null")
    private ContentType contentType;

    @NotNull(message = "like type can not be null")
    private LikeType likeType;

}