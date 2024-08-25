package com.jellybrains.quietspace_backend_ms.feedservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jellybrains.quietspace.common_service.entity.BaseEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PollOption extends BaseEntity {

    @NotNull
    @JsonIgnore
    @ManyToOne
    private Poll poll;

    @NotNull
    private String label;

    @NotNull
    @ElementCollection
    private Set<String> votes = new HashSet<>();

}
