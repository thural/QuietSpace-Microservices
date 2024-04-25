package com.jellybrains.quietspace_backend_ms.chatservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private Long id;

    @Version
    private Integer version;

    @ManyToOne
    private Chat chat;

    @NonNull
    private UUID senderId;

    @NonNull
    private String text;

    @NonNull
    @Column(updatable = false)
    private OffsetDateTime createDate;

    @NonNull
    private OffsetDateTime updateDate;

    @PrePersist
    private void onCreate() {
        createDate = OffsetDateTime.now();
        updateDate = OffsetDateTime.now();
    }

    @PreUpdate
    private void onUpdate() {
        updateDate = OffsetDateTime.now();
    }

}