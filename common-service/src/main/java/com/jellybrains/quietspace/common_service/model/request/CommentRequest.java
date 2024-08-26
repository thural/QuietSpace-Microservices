package com.jellybrains.quietspace.common_service.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest {

    @NotNull(message = "user id can not be null")
    private String userId;

    @NotNull(message = "post id can not be null")
    private String postId;

    private String parentId;

    @NotBlank(message = "comment text can not be blank")
    @Size(min = 1, max = 255, message = "at least 1 and max 255 characters expected")
    private String text;

}
