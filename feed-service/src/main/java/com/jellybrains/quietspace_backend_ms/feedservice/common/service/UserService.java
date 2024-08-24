package com.jellybrains.quietspace_backend_ms.feedservice.common.service;

import com.jellybrains.quietspace_backend_ms.feedservice.common.client.UserClient;
import com.jellybrains.quietspace_backend_ms.feedservice.common.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserClient userClient;
    private final HttpServletRequest request;

    public void validateUserId(String userId){
        if(userClient.validateUserId(userId))
            throw new UserNotFoundException();
    }

    public String getUsernameById(String userId) {
        return ""; // TODO: implement kafka method
    }

    public String getAuthorizedUserId(){
        return request.getHeader("userId");
    }

    public String getAuthorizedUsername(){
        return request.getHeader("username");
    }

    public String getAuthorizedUserFullName(){
        return request.getHeader("fullName");
    }
}
