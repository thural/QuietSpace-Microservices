package com.jellybrains.quietspace_backend_ms.chatservice.common;

import com.jellybrains.quietspace_backend_ms.chatservice.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserService {

    private final HttpServletRequest request;

    public void validateUserId(String userId){
        // TODO: check if requested user exists either by kafka or webclient
        if(true)
            throw new UserNotFoundException();
    }

    public String getAuthorizedUserId(){
        return request.getHeader("userId");
    }
}
