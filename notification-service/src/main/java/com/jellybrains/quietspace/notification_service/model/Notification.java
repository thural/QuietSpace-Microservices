package com.jellybrains.quietspace.notification_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jellybrains.quietspace.common_service.enums.ContentType;
import com.jellybrains.quietspace.common_service.enums.NotificationType;
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
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    @NotNull(message = "notification content can not be null")
    private String contentId;

    @Indexed
    @NotNull(message = "notification sender can not be null")
    private String actorId;

    @Indexed
    @JsonIgnore
    @NotNull(message = "notification receiver can not be null")
    private String userId;

    @NotNull(message = "notification username can not be null")
    @NotBlank(message = "notification username can not be blank")
    @Transient
    private String username;

    @NotBlank(message = "notification message can not be blank")
    private String message;

    @Builder.Default
    @NotNull(message = "notification seen state can not be null")
    private Boolean seen = Boolean.FALSE;

    @JsonIgnore
    @NotNull(message = "notification content type can not be null")
    private ContentType contentType;

    @NotNull(message = "notification type cna not be null")
    private NotificationType notificationType;

    @CreatedDate
    @NotNull(message = "notification create date can not be null")
    private OffsetDateTime createdDate;

    @LastModifiedDate
    @NotNull(message = "notification update date can not be null")
    private OffsetDateTime updateDDate;

}