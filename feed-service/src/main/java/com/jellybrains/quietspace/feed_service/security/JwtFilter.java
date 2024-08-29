package com.jellybrains.quietspace.feed_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
        String authHeader = request.getHeader("Authorization");


        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer "))
            throw new RuntimeException("authentication failed, bearer token is required");

        try {
            String token = authHeader.substring(7);

            if (jwtUtil.isTokenExpired(token))
                throw new RuntimeException("access token has expired");

            String username = jwtUtil.extractUsername(token);

            mutableRequest.putHeader("username", jwtUtil.extractUsername(token));
            mutableRequest.putHeader("userId", jwtUtil.extractUserId(token));
            mutableRequest.putHeader("fullName", jwtUtil.extractFullName(token));

            List<SimpleGrantedAuthority> authorities = jwtUtil.getAuthoritiesFromToken(token);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username, null, authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch( RuntimeException e){
            if (e.getMessage().equals("access token has expired"))
                throw new RuntimeException(e.getMessage());

            throw new ServletException("failed to authenticate, invalid jwt token");
        }

        filterChain.doFilter(mutableRequest, response);
    }

}