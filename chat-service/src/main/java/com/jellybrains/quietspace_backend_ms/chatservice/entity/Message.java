package com.jellybrains.quietspace_backend_ms.chatservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Message extends BaseEntity {

    @ManyToOne
    private Chat chat;

    @NotNull
    @ManyToOne
    private String senderId;

    @NotNull
    @ManyToOne
    private String recipientId;

    @NotNull
    @NotBlank
    private String text;

    @NotNull
    private Boolean isSeen;

    @PrePersist
    void initFields() {
        setIsSeen(false);
    }

}