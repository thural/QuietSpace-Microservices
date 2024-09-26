package com.jellybrains.quietspace.common_service.webclient.client;

import com.jellybrains.quietspace.common_service.model.response.UserResponse;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserClient {

    CompletableFuture<Boolean> validateUserId(String userId);

    CompletableFuture<Optional<UserResponse>> getLoggedUser();

    CompletableFuture<Optional<UserResponse>> getUserById(String userId);

    CompletableFuture<Boolean> validateUserIdList(List<String> userIds);

    CompletableFuture<List<UserResponse>> getUsersFromIdList(List<String> userIds);
}
