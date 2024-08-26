package com.jellybrains.quietspace.common_service.model.response;

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