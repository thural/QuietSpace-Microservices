package com.jellybrains.quietspace.user_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jellybrains.quietspace.common_service.enums.StatusType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Profile extends BaseEntity {

    @NotNull
    String userId;
    @NotNull
    String username;
    @NotNull
    String email;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> followingUserIds;
    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> followerUserIds;
    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> blockedUserIds;

    @JsonIgnore
    private String firstname;
    @JsonIgnore
    private String lastname;
    @JsonIgnore
    private OffsetDateTime dateOfBirth;
    @JsonIgnore
    private StatusType statusType;


    public String getFullName() {
        return firstname + " " + lastname;
    }

    @PrePersist
    void initProfile() {
        setStatusType(StatusType.OFFLINE);
    }

}