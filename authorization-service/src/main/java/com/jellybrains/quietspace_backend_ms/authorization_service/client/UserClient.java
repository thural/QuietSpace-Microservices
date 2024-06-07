package com.jellybrains.quietspace_backend_ms.authorization_service.client;

import com.jellybrains.quietspace_backend_ms.authorization_service.entity.UserRepresentation;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.request.UserRequest;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.response.UserResponse;

import java.util.Optional;
import java.util.UUID;

public interface UserClient {

    Boolean validateUserId(UUID userId);

    Optional<UserResponse> getLoggedUser();

    Optional<UserResponse> getUserById(UUID userId);

    Optional<UserResponse> getUserByEmail(String email);

    Optional <UserResponse> createUser(UserRequest userRequest);

    Optional<UserRepresentation> getUserRepresentationByEmail(String username);

    Boolean deleteById(UUID userId);
}
