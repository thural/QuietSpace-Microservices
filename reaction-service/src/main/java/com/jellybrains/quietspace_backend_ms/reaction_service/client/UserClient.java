package com.jellybrains.quietspace_backend_ms.reaction_service.client;

import com.jellybrains.quietspace_backend_ms.reaction_service.model.response.UserResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserClient {

    public Boolean validateUserId(UUID userId);

    public UserResponse getLoggedUser();

    public Mono<UserResponse> getUserById(UUID  userId);

}
