package com.jellybrains.quietspace.common_service.service.shared;

import com.jellybrains.quietspace.common_service.enums.StatusType;
import com.jellybrains.quietspace.common_service.exception.UserNotFoundException;
import com.jellybrains.quietspace.common_service.model.response.UserResponse;
import com.jellybrains.quietspace.common_service.webclient.client.UserClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
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

    @TimeLimiter(name = "user-service")
    @CircuitBreaker(name = "user-service")
    public void validateUserId(String userId) {
        if (userClient.validateUserId(userId).join()) throw new UserNotFoundException();
    }

    @TimeLimiter(name = "user-service")
    @CircuitBreaker(name = "user-service")
    public String getUsernameById(String userId) {
        return userClient.getUserById(userId).thenApply(optional -> optional.map(UserResponse::getUsername)
                .orElseThrow(UserNotFoundException::new)).join();
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

    @TimeLimiter(name = "user-service")
    @CircuitBreaker(name = "user-service")
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
