package com.jellybrains.quietspace_backend_ms.reaction_service.model.response;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String id;
    private String role;
    private String username;
    private String email;
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;

}