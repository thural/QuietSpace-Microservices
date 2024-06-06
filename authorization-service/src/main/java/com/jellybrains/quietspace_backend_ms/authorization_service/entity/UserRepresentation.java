package com.jellybrains.quietspace_backend_ms.authorization_service.entity;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
public class UserRepresentation {

    private UUID id;

    private String role;

    private String username;

    private String email;

    private String password;

}