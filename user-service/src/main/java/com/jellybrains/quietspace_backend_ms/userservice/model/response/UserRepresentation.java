package com.jellybrains.quietspace_backend_ms.userservice.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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