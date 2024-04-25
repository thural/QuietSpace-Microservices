package com.jellybrains.quietspace_backend_ms.feedservice.dto.request;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {

    @NonNull
    private UUID userId;
    @NonNull
    private String textContent;

}