package com.jellybrains.quietspace_backend_ms.feedservice.client;

import com.jellybrains.quietspace_backend_ms.feedservice.model.response.UserResponse;

import java.util.Optional;
import java.util.UUID;

public interface UserClient {

    Boolean validateUserId(UUID userId);

    Optional<UserResponse> getLoggedUser();

    Optional<UserResponse> getUserById(UUID userId);

}
