package com.jellybrains.quietspace_backend_ms.feedservice.dto.request;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeRequest {

    @NonNull
    private UUID userId;
    @NonNull
    private UUID postId;

}
