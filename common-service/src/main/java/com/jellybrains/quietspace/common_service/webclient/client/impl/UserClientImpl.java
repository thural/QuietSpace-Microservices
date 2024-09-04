package com.jellybrains.quietspace.common_service.webclient.client.impl;

import com.jellybrains.quietspace.common_service.model.response.UserResponse;
import com.jellybrains.quietspace.common_service.webclient.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserClientImpl implements UserClient {

    private final WebClient webClient;
    private final String USER_API_URI = "/api/v1/users/";

    @Override
    public Boolean validateUserId(String userId) {
        return webClient.get()
                .uri(USER_API_URI + "validate/" + userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    @Override
    public Optional<UserResponse> getLoggedUser() {
        return webClient.get()
                .uri(USER_API_URI + "user")
                .retrieve()
                .bodyToMono(UserResponse.class)
                .blockOptional();
    }


    @Retryable(maxAttempts = 4, backoff = @Backoff(delay = 1000, multiplier = 2))
    public Optional<UserResponse> getUserById(String id) {
        return webClient.get()
                .uri(USER_API_URI + id)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .blockOptional();
    }

    @Override
    public Boolean validateUserIdList(List<String> userIds) {
        return webClient.get()
                .uri(USER_API_URI + "validate/list",
                        uriBuilder -> uriBuilder.queryParam("userIds", userIds).build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    @Override
    public List<UserResponse> getUsersFromIdList(List<String> userIds) {
        return webClient.get()
                .uri(USER_API_URI + "getUsersFromList")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserResponse>>() {
                })
                .block();
    }

}
