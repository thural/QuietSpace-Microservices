package com.jellybrains.quietspace.user_service.webclient.client;

import com.jellybrains.quietspace.common_service.model.response.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserClient {

    Boolean validateUserId(String userId);

    Optional<UserResponse> getLoggedUser();

    Optional<UserResponse> getUserById(String userId);

    Boolean validateUserIdList(List<String> userIds);

    List<UserResponse> getUsersFromIdList(List<String> userIds);
}
