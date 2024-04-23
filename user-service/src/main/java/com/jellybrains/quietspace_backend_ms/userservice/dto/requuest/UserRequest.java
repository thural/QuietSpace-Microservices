package com.jellybrains.quietspace_backend_ms.userservice.dto.requuest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserRequest {
    private String username;
    private String password;
    private String email;
    private String role;
}
