package com.jellybrains.quietspace_backend_ms.userservice.common.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jellybrains.quietspace_backend_ms.userservice.common.enums.StatusType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

    private String userId;
    private String username;
    private String email;

    private List<String> followingUserIds;
    private List<String> followerUserIds;
    private List<String> blockedUserIds;

    private String firstname;
    private String lastname;
    private OffsetDateTime dateOfBirth;
    private StatusType statusType;

}