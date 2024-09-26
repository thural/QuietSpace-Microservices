package com.jellybrains.quietspace.common_service.websocket.config;

import com.jellybrains.quietspace.common_service.service.shared.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
class CustomHandshakeHandler extends DefaultHandshakeHandler {

    private final UserService userService;

    @Override
    protected Principal determineUser(
            ServerHttpRequest request,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {
        String authorizedUserId = userService.getAuthorizedUserId();
        log.info("userId at CustomHandshakeHandler: {}", authorizedUserId);
        return new StompPrincipal(authorizedUserId);
    }

}
