package com.jellybrains.quietspace.common_service.webclient.service;

import com.jellybrains.quietspace.common_service.enums.StatusType;
import com.jellybrains.quietspace.common_service.exception.UserNotFoundException;
import com.jellybrains.quietspace.common_service.model.response.UserResponse;
import com.jellybrains.quietspace.common_service.webclient.client.UserClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserClient userClient;
    private final HttpServletRequest request;

    public void validateUserId(String userId) {
        if (userClient.validateUserId(userId)) throw new UserNotFoundException();
    }

    public String getUsernameById(String userId) {
        return userClient.getUserById(userId).map(UserResponse::getUsername)
                .orElseThrow(UserNotFoundException::new);
    }

    public String getAuthorizedUserId() {
        return request.getHeader("userId");
    }

    public String getAuthorizedUsername() {
        return request.getHeader("username");
    }

    public String getAuthorizedUserFullName() {
        return request.getHeader("fullName");
    }

    public void setOnlineStatus(String username, StatusType statusType) {
    }

    public Map<String, String> getUserClaims() {
        Map<String, String> claims = new HashMap<>();
        claims.put("userId", getAuthorizedUserId());
        claims.put("username", getAuthorizedUsername());
        claims.put("userFullName", getAuthorizedUserFullName());
        return claims;
    }

}
