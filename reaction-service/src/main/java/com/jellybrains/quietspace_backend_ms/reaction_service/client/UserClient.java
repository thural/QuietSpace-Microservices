package com.jellybrains.quietspace_backend_ms.reaction_service.client;

import com.jellybrains.quietspace_backend_ms.reaction_service.model.response.UserResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface UserClient {

    Boolean validateUserId(String userId);

    Optional<UserResponse> getLoggedUser();

    Optional<UserResponse> getUserById(String userId);

}
