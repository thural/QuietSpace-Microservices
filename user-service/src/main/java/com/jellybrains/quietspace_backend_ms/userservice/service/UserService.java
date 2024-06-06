package com.jellybrains.quietspace_backend_ms.userservice.service;

import com.jellybrains.quietspace_backend_ms.userservice.entity.User;
import com.jellybrains.quietspace_backend_ms.userservice.model.request.UserRequest;
import com.jellybrains.quietspace_backend_ms.userservice.model.response.UserRepresentation;
import com.jellybrains.quietspace_backend_ms.userservice.model.response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    Page<UserResponse> listUsers(String username, Integer pageNumber, Integer pageSize);

    Page<UserResponse> listUsersByQuery(String query, Integer pageNumber, Integer pageSize);

    List<User> getUsersFromIdList(List<UUID> userIds);

    Optional<UserResponse> getUserResponseById(UUID id);

    Optional<User> getUserById(UUID userId);

    Optional<UserResponse> createUser(UserRequest userRequest);

    Optional<UserResponse> getUserByEmail(String email);

    void deleteUser(UUID userId, String authHeader);

    UserResponse patchUser(UserRequest userRequest);

    Optional<UserResponse> getLoggedUserResponse();

    User getAuthorizedUser();

    Boolean validateUserInRequest(UUID userId);

    Boolean validateUsersByIdList(List<UUID> userIds);

    Optional<UserRepresentation> getUserRepresentationByEmail(String email);
}
