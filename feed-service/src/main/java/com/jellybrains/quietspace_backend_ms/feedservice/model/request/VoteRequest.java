package com.jellybrains.quietspace_backend_ms.feedservice.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteRequest {

    @NotNull(message = "user id can not be null")
    private String userId;

    @NotNull(message = "post id can not be null")
    private String postId;

    @NotNull(message = "vote label can not be null")
    private String option;

}