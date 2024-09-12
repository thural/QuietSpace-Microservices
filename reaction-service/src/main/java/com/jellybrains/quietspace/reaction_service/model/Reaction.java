package com.jellybrains.quietspace.reaction_service.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.ReactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
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

    @Transient
    @NotNull
    @NotBlank
    private String username;

    @Indexed
    @NotNull(message = "reaction content id can not be null")
    private String contentId;

    @JsonIgnore
    @NotNull(message = "reaction content type can not be null")
    private ContentType contentType;

    @NotNull(message = "reaction type can not be null")
    private ReactionType reactionType;


    @CreatedDate
    private OffsetDateTime createdDate;

    @LastModifiedDate
    private OffsetDateTime updatedDate;

}
