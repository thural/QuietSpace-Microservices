package com.jellybrains.quietspace_backend_ms.authorization_service.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class AuthenticationResponse {

    private UUID id;
    private String token;
    private String userId;
    private String message;

}
