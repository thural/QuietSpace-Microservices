package com.jellybrains.quietspace_backend_ms.authorization_service.command.service;

import com.jellybrains.quietspace_backend_ms.authorization_service.command.controller.model.CreateUserCommand;
import com.jellybrains.quietspace_backend_ms.authorization_service.event.deletion.DeleteUserEvent;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.User;
import com.jellybrains.quietspace_backend_ms.authorization_service.service.KeycloakService;
import com.jellybrains.quietspace_backend_ms.authorization_service.util.UserCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.jellybrains.quietspace_backend_ms.authorization_service.service.KeycloakService.createUserRepresentation;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCommandService {

    private final UserCreator userCreator;
    private final KeycloakService keycloakService;
    private final ReactiveKafkaProducerTemplate<String, DeleteUserEvent> kafkaProducerTemplate;

    @Value("${kafka.topics.delete-user-profile-request}")
    private String deleteUserProfileRequest;

    @Retryable
    public Mono<User> createUser(CreateUserCommand command) {

        UserRepresentation user = createUserRepresentation(command);
        String id = command.id().toString();
        user.setId(id);

        return userCreator.createUserFromRepresentation(Mono.just(user));
    }

    public Mono<Void> requestUserDeletionById(UUID userId) {
        return kafkaProducerTemplate.send(
                        deleteUserProfileRequest,
                        new DeleteUserEvent(userId))
                .then();
    }

    public void verifyEmail(String userId) {

        UsersResource usersResource = keycloakService.getUsersResource();
        usersResource.get(userId).sendVerifyEmail();
    }

    public Mono<Void> updatePassword(String userId) {

        UserResource userResource = keycloakService.getUsersResource().get(userId);
        List<String> actions = new ArrayList<>();
        actions.add("UPDATE_PASSWORD");

        return Mono.fromRunnable(() -> userResource.executeActionsEmail(actions));
    }
}
