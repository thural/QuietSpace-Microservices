package com.jellybrains.quietspace_backend_ms.authorization_service.keycloak.service;


import com.jellybrains.quietspace_backend_ms.authorization_service.exception.EmailAlreadyExistsException;
import com.jellybrains.quietspace_backend_ms.authorization_service.exception.UnexpectedResponseStatusException;
import com.jellybrains.quietspace_backend_ms.authorization_service.keycloak.model.User;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Slf4j
@RequiredArgsConstructor
@Component
public class UserCreator {

    private final KeycloakService keycloakService;

    public Mono<User> createUserFromRepresentation(Mono<UserRepresentation> userRepresentation) {
        return userRepresentation
                .flatMap(this::createUserInKeycloak)
                .log();
    }

    private Mono<User> createUserInKeycloak(UserRepresentation userRepresentation) {
        Response response = keycloakService.getUsersResource().create(userRepresentation);
        return handleKeycloakResponse(response, userRepresentation);
    }

    private Mono<User> handleKeycloakResponse(Response response, UserRepresentation userRepresentation) {
        switch (response.getStatus()) {
            case 201:
                String userId = CreatedResponseUtil.getCreatedId(response);
                keycloakService.addRole(userId);
                return Mono.just(new User(userId, userRepresentation.getEmail()));
            case 409:
                throw new EmailAlreadyExistsException(userRepresentation.getEmail());
            default:
                throw new UnexpectedResponseStatusException(response.getStatus());
        }
    }
}
