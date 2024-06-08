package com.jellybrains.quietspace_backend_ms.authorization_service.command.controller.model;

import java.util.UUID;

public record CreateUserCommand(UUID id,
                                String password,
                                String email) {
}
