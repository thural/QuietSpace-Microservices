package com.jellybrains.quietspace_backend_ms.chatservice.client;


import com.jellybrains.quietspace_backend_ms.chatservice.model.response.UserResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserClient {

    Boolean validateUserId(UUID userId);

    Optional<UserResponse> getLoggedUser();

    Optional<UserResponse> getUserById(UUID userId);

    Boolean validateUserIdList(List<UUID> userIds);
}
