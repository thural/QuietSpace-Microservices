package com.jellybrains.quietspace.feed_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
public class Comment extends BaseEntity {

    private String parentId;

    @NotNull
    @NotBlank
    private String text;

    @NotNull
    @JsonIgnore
    private String userId;

    @NotNull
    @ManyToOne
    @JsonIgnore
    private Post post;

}
