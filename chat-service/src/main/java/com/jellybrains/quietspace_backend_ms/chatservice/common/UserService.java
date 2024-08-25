package com.jellybrains.quietspace_backend_ms.chatservice.common;

import com.jellybrains.quietspace_backend_ms.chatservice.client.UserClient;
import com.jellybrains.quietspace_backend_ms.chatservice.exception.UserNotFoundException;
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

    public String getAuthorizedUserId(){
        return request.getHeader("userId");
    }
}
