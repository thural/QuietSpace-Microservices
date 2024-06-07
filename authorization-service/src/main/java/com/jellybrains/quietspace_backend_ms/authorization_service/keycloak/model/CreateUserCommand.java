package com.jellybrains.quietspace_backend_ms.authorization_service.keycloak.model;

import java.util.UUID;

public record CreateUserCommand(UUID id,
                                String password,
                                String email) {
}
