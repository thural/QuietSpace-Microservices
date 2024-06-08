package com.jellybrains.quietspace_backend_ms.authorization_service.service;


import com.jellybrains.quietspace_backend_ms.authorization_service.command.controller.model.CreateUserCommand;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.User;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private static final String ROLE_USER = "ROLE_USER";

    private final Keycloak keycloakClient;

    @Value("${keycloak.realm}")
    private String realm;

    public static UserRepresentation createUserRepresentation(CreateUserCommand request) {
        UserRepresentation user = new UserRepresentation();
        user.setEmail(request.email());
        user.setEmailVerified(false);
        user.setEnabled(true);

        setCredentials(user, request.password());

        return user;
    }

    public UsersResource getUsersResource() {
        return keycloakClient
                .realm(realm)
                .users();
    }

    public void addRole(String userId) {
        UserResource userResource = getUsersResource().get(userId);
        var role = keycloakClient.realm(realm)
                .roles()
                .get(ROLE_USER)
                .toRepresentation();

        userResource.roles()
                .realmLevel()
                .add(List.of(role));
    }

    private static void setCredentials(UserRepresentation user, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        user.setCredentials(List.of(credential));
        user.setRealmRoles(List.of(ROLE_USER));
    }

    public static User getUserFromRepresentation(UserRepresentation ur) {
        return User.builder()
                .id(ur.getId())
                .email(ur.getEmail())
                .build();
    }

}