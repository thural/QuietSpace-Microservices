package com.jellybrains.quietspace_backend_ms.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private String role;
}