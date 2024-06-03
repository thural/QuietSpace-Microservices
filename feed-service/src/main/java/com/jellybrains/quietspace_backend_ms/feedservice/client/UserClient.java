package com.jellybrains.quietspace_backend_ms.feedservice.client;

import com.jellybrains.quietspace_backend_ms.feedservice.model.response.UserResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserClient {

    public Boolean validateUserId(UUID userId);

    public UserResponse getLoggedUser();

    public Mono<UserResponse> getUserById(UUID  userId);

}
