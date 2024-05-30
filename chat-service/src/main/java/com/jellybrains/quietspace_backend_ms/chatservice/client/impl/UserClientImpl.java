package com.jellybrains.quietspace_backend_ms.chatservice.client.impl;

import com.jellybrains.quietspace_backend_ms.chatservice.client.UserClient;
import com.jellybrains.quietspace_backend_ms.chatservice.model.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserClientImpl implements UserClient {

    @Override
    public Boolean validateUserId(UUID userId){
        return true; // TODO: use webclient
        // TODO: use AOP logic if you can
    }

    @Override
    public Optional<UserResponse> getLoggedUser(){
        return null; // TODO: use webclient
    }

    @Override
    public Optional<UserResponse> getUserById(UUID userId) {
        return null; // TODO: use webclient
    }

    @Override
    public boolean validateUserIdList(List<UUID> userIds) {
        return true; // TODO: use webclient
    }

}
