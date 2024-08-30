package com.jellybrains.quietspace.notification_service.webclient.service;

import com.jellybrains.quietspace.common_service.enums.StatusType;
import com.jellybrains.quietspace.notification_service.exception.UserNotFoundException;
import com.jellybrains.quietspace.notification_service.webclient.client.UserClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserClient userClient;
    private final HttpServletRequest request;

    public void validateUserId(String userId){
        if(userClient.validateUserId(userId)) throw new UserNotFoundException();
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

    public void setOnlineStatus(String username, StatusType statusType) {
        // TODO: implement kafka
    }

}
