package com.jellybrains.quietspace_backend_ms.reaction_service.model;


import com.jellybrains.quietspace_backend_ms.reaction_service.utils.enums.ContentType;
import com.jellybrains.quietspace_backend_ms.reaction_service.utils.enums.LikeType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reactions")
public class Reaction {
    @Id
    private String id;

    @Indexed
    @NotNull(message = "reaction user id can not be null")
    private String userId;

    @Indexed
    @NotNull(message = "reaction content id can not be null")
    private String contentId;

    @NotNull(message = "reaction content type can not be null")
    private ContentType contentType;

    @NotNull(message = "reaction like type can not be null")
    private LikeType likeType;


    @CreatedDate
    @NotNull(message = "reaction create date can not be null")
    private OffsetDateTime createdDate;
    @LastModifiedDate
    private OffsetDateTime updateDDate;
}
