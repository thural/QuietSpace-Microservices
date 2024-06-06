package com.jellybrains.quietspace_backend_ms.authorization_service.model.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private UUID id;
    private String token;
    private String userId;
    private String message;

}
