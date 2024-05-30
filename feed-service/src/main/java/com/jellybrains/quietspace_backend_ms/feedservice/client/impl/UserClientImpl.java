package com.jellybrains.quietspace_backend_ms.feedservice.client.impl;

import com.jellybrains.quietspace_backend_ms.feedservice.model.response.UserResponse;
import com.jellybrains.quietspace_backend_ms.feedservice.client.UserClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserClientImpl implements UserClient {

    @Override
    public Boolean validateUserId(UUID userId){
        return true; // TODO: use webclient
        // TODO: use AOP logic if you can
    }

    @Override
    public UserResponse getLoggedUser(){
        return null; // TODO: use webclient
    }

    @Override
    public UserResponse getUserById(UUID userId) {
        return null; // TODO: use webclient
    }

}
