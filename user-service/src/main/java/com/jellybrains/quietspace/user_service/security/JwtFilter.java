package com.jellybrains.quietspace.user_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("authentication failed, bearer token is required");
        }

        try {
            String jwtToken = authHeader.substring(7);
            if (jwtService.isTokenExpired(jwtToken)){
                throw new RuntimeException("access token has expired");
            }
        }
        catch( RuntimeException e){
            if (e.getMessage().equals("access token has expired")){
                throw new RuntimeException(e.getMessage());
            }
            throw new ServletException("failed to authenticate, invalid jwt token");
        }

        filterChain.doFilter(request, response);
    }

}