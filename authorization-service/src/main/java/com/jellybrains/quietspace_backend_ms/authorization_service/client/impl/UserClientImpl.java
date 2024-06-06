package com.jellybrains.quietspace_backend_ms.authorization_service.client.impl;

import com.jellybrains.quietspace_backend_ms.authorization_service.client.UserClient;
import com.jellybrains.quietspace_backend_ms.authorization_service.entity.UserRepresentation;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.request.UserRequest;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserClientImpl implements UserClient {

    private final WebClient webClient;
    private final String USER_API_URI = "/api/v1/user/";

    @Override
    public Boolean validateUserId(UUID userId){
        return webClient.get()
                .uri(USER_API_URI + "validate/" + userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        // TODO: use AOP logic if you can
    }

    @Override
    public Optional<UserResponse> getLoggedUser(){
        return webClient.get()
                .uri(USER_API_URI + "/user")
                .retrieve()
                .bodyToMono(UserResponse.class)
                .blockOptional();
    }


    @Retryable(maxAttempts = 4, backoff = @Backoff(delay = 1000, multiplier = 2))
    public Optional<UserResponse> getUserById(UUID id) {
        return webClient.get()
                .uri(USER_API_URI + id)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .blockOptional();
    }

    @Override
    public Optional<UserResponse> getUserByEmail(String email) {
        return webClient.get()
                .uri(USER_API_URI + "/by-email/" + email)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .blockOptional();
    }

    @Override
    public Optional<UserResponse> createUser(UserRequest userRequest) {
        return webClient.post()
                .uri("/api/v1/user")
                .bodyValue(userRequest)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .blockOptional();
    }

    @Override
    public Optional<UserRepresentation> getUserRepresentationByEmail(String email) {
        return webClient.get()
                .uri(USER_API_URI + "/representation/by-email/" + email)
                .retrieve()
                .bodyToMono(UserRepresentation.class)
                .blockOptional();
    }

}
