package com.jellybrains.quietspace_backend_ms.authorization_service.query.service;

import com.jellybrains.quietspace_backend_ms.authorization_service.model.User;
import com.jellybrains.quietspace_backend_ms.authorization_service.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.jellybrains.quietspace_backend_ms.authorization_service.service.KeycloakService.getUserFromRepresentation;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final Keycloak keycloakClient;

    @Value("${keycloak.realm}")
    private String realm;

    public List<User> getUsers() {
        RealmResource realmResource = keycloakClient.realm(realm);

        return realmResource.users()
                .list()
                .stream()
                .map(KeycloakService::getUserFromRepresentation)
                .toList();
    }

    public User getUserById(String id) {
        RealmResource realmResource = keycloakClient.realm(realm);

        UserRepresentation userRepresentation = realmResource.users()
                .get(id)
                .toRepresentation();

        return getUserFromRepresentation(userRepresentation);
    }

}
