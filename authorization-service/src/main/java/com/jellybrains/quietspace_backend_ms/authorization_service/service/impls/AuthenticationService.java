package com.jellybrains.quietspace_backend_ms.authorization_service.service.impls;

import com.jellybrains.quietspace_backend_ms.authorization_service.entity.Role;
import com.jellybrains.quietspace_backend_ms.authorization_service.entity.Token;
import com.jellybrains.quietspace_backend_ms.authorization_service.entity.User;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.request.AuthenticationRequest;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.request.RegistrationRequest;
import com.jellybrains.quietspace_backend_ms.authorization_service.model.response.AuthenticationResponse;
import com.jellybrains.quietspace_backend_ms.authorization_service.repository.RoleRepository;
import com.jellybrains.quietspace_backend_ms.authorization_service.repository.TokenRepository;
import com.jellybrains.quietspace_backend_ms.authorization_service.repository.UserRepository;
import com.jellybrains.quietspace_backend_ms.authorization_service.security.JwtService;
import com.jellybrains.quietspace_backend_ms.authorization_service.utils.enums.EmailTemplateName;
import com.jellybrains.quietspace_backend_ms.authorization_service.utils.enums.RoleType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;

    @Value("${spring.application.mailing.frontend.activation-url}")
    private String activationUrl;

    public AuthenticationResponse register(RegistrationRequest request) throws MessagingException {
        log.info("Registering new user with email: {}", request.getEmail());

        Role userRole = roleRepository.findByName(RoleType.USER.toString())
                // todo - better exception handling
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));

        User user = User.builder()
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(true) // TODO: rollback to false value after testing
                .role(RoleType.USER.toString())
                .roles(List.of(userRole))
                .build();

        User savedUser = userRepository.save(user);
//        sendValidationEmail(user);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        log.info("generating token for user: {}", savedUser.getUsername());

        var claims = new HashMap<String, Object>();
        claims.put("fullName", user.getFullName());
        String token = jwtService.generateToken(claims, user);

        return AuthenticationResponse.builder()
                .id(UUID.randomUUID())
                .message("registration successful")
                .token(token)
                .userId(savedUser.getId().toString())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        UserDetails userDetails = userRepository.findUserEntityByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));

        log.info("authenticating user: {}", userDetails.getUsername());

        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        request.getPassword()
                )
        );

        log.info("authenticated user: {}", auth.getPrincipal());

        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("fullName", user.getFullName());

        var jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());

        return AuthenticationResponse.builder()
                .id(UUID.randomUUID())
                .message("authentication successful")
                .userId(user.getId().toString())
                .token(jwtToken)
                .build();
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                // todo exception has to be defined
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .email(user.getEmail())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }

    private void sendValidationEmail(User user) throws MessagingException {
        log.info("sending to email address: {}", user.getEmail());
        var newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
}
