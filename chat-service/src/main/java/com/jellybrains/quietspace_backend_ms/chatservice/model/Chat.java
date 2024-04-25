package com.jellybrains.quietspace_backend_ms.chatservice.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private Long id;

    @Version
    private Integer version;

    //TODO: implement proper inter service relation for users
    List<UUID> userList;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

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
