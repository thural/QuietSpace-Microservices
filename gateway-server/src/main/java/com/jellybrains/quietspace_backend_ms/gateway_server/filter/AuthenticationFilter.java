package com.jellybrains.quietspace_backend_ms.gateway_server.filter;

import com.jellybrains.quietspace_backend_ms.gateway_server.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = null;
            if (validator.isSecured.test(exchange.getRequest())) {

                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                    throw new RuntimeException("missing authorization header");

                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
                if (authHeader == null || !authHeader.startsWith("Bearer "))
                    throw new RuntimeException("no bearer token is present");

                String jwtToken = authHeader.substring(7);

                if(!jwtUtil.isTokenValid(jwtToken))
                    throw new RuntimeException("unauthorized access to application");

                request = exchange.getRequest()
                        .mutate()
                        .header("username", jwtUtil.extractUsername(jwtToken))
                        .header("userId", jwtUtil.extractUserId(jwtToken))
                        .header("fullName", jwtUtil.extractFullName(jwtToken))
                        .build();
            }
            assert request != null;
            return chain.filter(exchange.mutate().request(request).build());
        });
    }

    public static class Config {

    }
}
