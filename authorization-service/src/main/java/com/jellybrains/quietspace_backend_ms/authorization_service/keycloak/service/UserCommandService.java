package com.jellybrains.quietspace_backend_ms.authorization_service.keycloak.service;

import com.jellybrains.quietspace_backend_ms.authorization_service.client.UserClient;
import com.jellybrains.quietspace_backend_ms.authorization_service.keycloak.model.CreateUserCommand;
import com.jellybrains.quietspace_backend_ms.authorization_service.keycloak.model.User;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.jellybrains.quietspace_backend_ms.authorization_service.keycloak.service.KeycloakService.createUserRepresentation;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCommandService {

    private final UserCreator userCreator;
    private final KeycloakService keycloakService;
    private final UserClient userClient;


    @Retryable
    public Mono<User> createUser(CreateUserCommand command) {

        UserRepresentation user = createUserRepresentation(command);
        String id = command.id().toString();
        user.setId(id);

        return userCreator.createUserFromRepresentation(Mono.just(user));
    }

    public Mono<Void> requestUserDeletionById(UUID userId) {
        deleteUserById(userId);
        return Mono.empty();
    }

    public void deleteUserById(UUID userId) {

        try (Response delete = keycloakService.getUsersResource().delete(userId.toString())) {

            if (delete.getStatus() == 204) {
                userClient.deleteById(userId);
                log.info("User deletion successful for userId: {}", userId);
            } else {
                throw new InternalServerErrorException("Deletion failed. Response Status is " + delete.getStatus());
            }

        } catch (Exception e) {
            log.info("User deletion failed for userId: {} due to: {}", userId, e.getMessage());
        }

    }

    public void verifyEmail(String userId) {
        UsersResource usersResource = keycloakService.getUsersResource();
        usersResource.get(userId).sendVerifyEmail();
    }

    public Mono<Void> updatePassword(String userId) {
        UserResource userResource = keycloakService.getUsersResource().get(userId);

        UserRepresentation user = userResource.toRepresentation();

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue("newPassword"); // TODO: implement a method to get new password
        credential.setTemporary(false);

        user.setCredentials(Collections.singletonList(credential));
        userResource.update(user);

        List<String> actions = new ArrayList<>();
        actions.add("UPDATE_PASSWORD");

        return Mono.fromRunnable(() -> userResource.executeActionsEmail(actions));
    }
}
