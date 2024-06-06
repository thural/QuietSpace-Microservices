package com.jellybrains.quietspace_backend_ms.authorization_service.service.impls;

import com.jellybrains.quietspace_backend_ms.authorization_service.client.UserClient;
import com.jellybrains.quietspace_backend_ms.authorization_service.entity.Token;
import com.jellybrains.quietspace_backend_ms.authorization_service.exception.UserCreationFailed;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.request.LoginRequest;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.request.UserRequest;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.response.AuthResponse;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.response.UserResponse;
import com.jellybrains.quietspace_backend_ms.authorization_service.repository.TokenRepository;
import com.jellybrains.quietspace_backend_ms.authorization_service.service.AuthService;
import com.jellybrains.quietspace_backend_ms.authorization_service.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final UserClient userClient;


    @Override
    public AuthResponse register(UserRequest userRequest) {
        String userPassword = userRequest.getPassword();
        userRequest.setPassword(passwordEncoder.encode(userPassword));

        UserResponse savedUser = userClient.createUser(userRequest)
                .orElseThrow(UserCreationFailed::new);

        Authentication authentication = generateAuthentication(userRequest.getEmail(), userPassword);

        String token = JwtProvider.generateToken(authentication);
        String userId = savedUser.getId().toString();
        return new AuthResponse(UUID.randomUUID(), token, userId, "register success");
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        String userEmail = loginRequest.getEmail();
        String userPassword = loginRequest.getPassword();
        Authentication authentication = generateAuthentication(userEmail, userPassword);
        String token;

        if(tokenRepository.existsByEmail(userEmail)){
            token = tokenRepository.getByEmail(userEmail).getJwtToken();
            tokenRepository.deleteByEmail(userEmail);
        } else {
            token = JwtProvider.generateToken(authentication);
        }

        Optional<UserResponse> optionalUser = userClient.getUserByEmail(userEmail);
        UserResponse user = optionalUser.orElseThrow(() -> new AuthenticationCredentialsNotFoundException("login failed"));
        return new AuthResponse(UUID.randomUUID(), token, user.getId().toString(), "login success");
    }

    @Override
    public void logout(String authHeader) {
        String currentUserName = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            addToBlacklist(authHeader, currentUserName);
            SecurityContextHolder.clearContext();
        }
    }

    @Override
    public Authentication generateAuthentication(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (userDetails == null)
            throw new BadCredentialsException("invalid username");

        if (!passwordEncoder.matches(password, userDetails.getPassword()))
            throw new BadCredentialsException("invalid password");

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities());
    }

    @Override
    public void addToBlacklist(String authHeader, String email) {
        String token = authHeader.substring(7);
        boolean isBlacklisted = tokenRepository.existsByJwtToken(token);
        if (!isBlacklisted) tokenRepository.save(Token.builder()
                .jwtToken(token)
                .email(email)
                .build()
        );
        SecurityContextHolder.clearContext();
    }

    @Override
    public boolean isBlacklisted(String authHeader) {
        String token = authHeader.substring(7);
        return tokenRepository.existsByJwtToken(token);
    }

}