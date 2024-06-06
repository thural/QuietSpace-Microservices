package com.jellybrains.quietspace_backend_ms.authorization_service.service;

import com.jellybrains.quietspace_backend_ms.authorization_service.model.request.LoginRequest;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.request.UserRequest;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.response.AuthResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {

    AuthResponse register(UserRequest user);

    AuthResponse login(LoginRequest loginRequest);

    void logout(String authHeader);

    Authentication generateAuthentication(String email, String password);

    void addToBlacklist(String authHeader, String email);

    boolean isBlacklisted(String authHeader);

}
