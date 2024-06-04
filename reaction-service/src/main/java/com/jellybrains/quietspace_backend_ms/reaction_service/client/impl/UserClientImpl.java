package com.jellybrains.quietspace_backend_ms.reaction_service.client.impl;

import com.jellybrains.quietspace_backend_ms.reaction_service.client.UserClient;
import com.jellybrains.quietspace_backend_ms.reaction_service.model.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserClientImpl implements UserClient {

    private final WebClient webClient;
    private final String USER_API_URI = "/api/v1/users/";

    @Override
    public Boolean validateUserId(String userId){
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
    public Optional<UserResponse> getUserById(String id) {
        return webClient.get()
                .uri(USER_API_URI + id)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .blockOptional();
    }

}
