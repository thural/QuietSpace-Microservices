package com.jellybrains.quietspace.common_service.model.response;

import com.jellybrains.quietspace.common_service.enums.StatusType;
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