package com.jellybrains.quietspace_backend_ms.notification_service.model;

import com.jellybrains.quietspace_backend_ms.notification_service.utils.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
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
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    @Indexed
    @NotNull(message = "notification sender can not be null")
    private String senderId;

    @Indexed
    @NotNull(message = "notification receiver can not be null")
    private String receiverId;

    @NotBlank(message = "notification message can not be blank")
    private String message;

    @Builder.Default
    @NotNull(message = "notification seen state can not be null")
    private Boolean seen = Boolean.FALSE;

    @NotNull(message = "notification type can not be null")
    private NotificationType notificationType;

    @CreatedDate
    @NotNull(message = "notification ")
    private OffsetDateTime createdDate;
    @LastModifiedDate
    private OffsetDateTime updateDDate;

}