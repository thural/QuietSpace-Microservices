package com.jellybrains.quietspace_backend_ms.feedservice.dto.request;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest {

    @NonNull
    UUID userId;
    @NonNull
    UUID postId;
    @NonNull
    private String text;

}
