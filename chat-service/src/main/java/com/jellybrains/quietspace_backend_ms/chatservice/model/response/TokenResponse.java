package com.jellybrains.quietspace_backend_ms.chatservice.model.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    private UUID id;
    private String jwtToken;

}