package com.jellybrains.quietspace.auth_service.service.impls;

import com.jellybrains.quietspace.auth_service.entity.Role;
import com.jellybrains.quietspace.auth_service.entity.Token;
import com.jellybrains.quietspace.auth_service.entity.User;
import com.jellybrains.quietspace.auth_service.exception.ActivationTokenException;
import com.jellybrains.quietspace.auth_service.exception.UnauthorizedException;
import com.jellybrains.quietspace.auth_service.exception.UserNotFoundException;
import com.jellybrains.quietspace.auth_service.model.AuthRequest;
import com.jellybrains.quietspace.auth_service.model.AuthResponse;
import com.jellybrains.quietspace.auth_service.model.RegistrationRequest;
import com.jellybrains.quietspace.auth_service.repository.RoleRepository;
import com.jellybrains.quietspace.auth_service.repository.TokenRepository;
import com.jellybrains.quietspace.auth_service.repository.UserRepository;
import com.jellybrains.quietspace.auth_service.security.JwtUtil;
import com.jellybrains.quietspace.common_service.enums.EmailTemplateName;
import com.jellybrains.quietspace.common_service.enums.RoleType;
import com.jellybrains.quietspace.common_service.enums.StatusType;
import com.jellybrains.quietspace.common_service.message.kafka.user.UserCreationEvent;
import com.jellybrains.quietspace.common_service.message.kafka.user.UserDeletionEvent;
import com.jellybrains.quietspace.common_service.rabbitmq.producer.UserProducer;
import com.jellybrains.quietspace.common_service.websocket.model.UserRepresentation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
    private final UserDetailsService userDetailsService;
    private final UserProducer userProducer;

    @Value("${spring.application.mailing.frontend.activation-url}")
    private String activationUrl;

    public User register(RegistrationRequest request) {
        log.info("registering new user with email: {}", request.getEmail());

        Role userRole = roleRepository.findByName(RoleType.USER.toString())
                .orElseThrow(() -> new IllegalStateException("ROLE USER has not been initiated"));

        User user = User.builder()
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        return userRepository.save(user);
    }

    public AuthResponse authenticate(AuthRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        log.info("authenticating user by email: {}", request.getEmail());
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        User user = ((User) auth.getPrincipal());
        claims.put("fullName", user.getFullName());
        claims.put("userId", user.getId());

        String jwtAccessToken = jwtUtil.generateToken(claims, user);
        String jwtRefreshToken = jwtUtil.generateRefreshToken(claims, user);
        log.info("generated jwt token during authentication: {}", jwtAccessToken);

        return AuthResponse.builder()
                .message("authentication was successful")
                .userId(user.getId())
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token existingToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ActivationTokenException("invalid token, please try again"));

        if (OffsetDateTime.now().isAfter(existingToken.getExpireDate())) {
            sendValidationEmail(existingToken.getUser());
            throw new RuntimeException("activation token has expired... a new token has been sent");
        }

        User user = userRepository.findById(existingToken.getUser().getId())
                .orElseThrow(UserNotFoundException::new);
        user.setEnabled(true);
        userRepository.save(user);

        existingToken.setValidateDate(OffsetDateTime.now());
        tokenRepository.save(existingToken);
    }

    public void signout(String authHeader) {
        String currentUserName = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        log.info("current username from security context on signing out: {}", currentUserName);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            addToBlacklist(authHeader, currentUserName);
            SecurityContextHolder.clearContext();
        }
    }

    private String generateAndSaveActivationToken(User user) {
        String activationCode = generateActivationCode(6);

        Token token = Token.builder()
                .token(activationCode)
                .email(user.getEmail())
                .expireDate(OffsetDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        tokenRepository.save(token);
        return activationCode;
    }

    public void sendValidationEmail(User user) throws MessagingException {
        log.info("sending activation code to email address: {}", user.getEmail());
        String newActivationCode = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newActivationCode,
                "account activation"
        );
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder generatedCode = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            generatedCode.append(characters.charAt(randomIndex));
        }

        log.info("generated activation token: {}", generatedCode);
        return generatedCode.toString();
    }

    public void addToBlacklist(String authHeader, String username) {
        String jwtToken = authHeader.substring(7);
        boolean isBlacklisted = tokenRepository.existsByToken(jwtToken);
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        if (!isBlacklisted) saveToken(jwtToken, user);
    }

    private void saveToken(String jwtToken, User user) {
        tokenRepository.save(Token.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .user(user)
                .build());
    }

    public AuthResponse refreshToken(String authHeader) {
        String refreshToken = authHeader.substring(7);

        AuthResponse authResponse = AuthResponse.builder()
                .refreshToken(refreshToken)
                .message("token refresh was failed")
                .build();

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) return authResponse;
        if (tokenRepository.existsByToken(refreshToken)) return authResponse;

        String username = jwtUtil.extractUsername(refreshToken);
        log.info("extracted username during jwt filtering: {}", username);
        if (username == null) return authResponse;

        User user = userRepository.findUserByUsername(username).orElseThrow(UserNotFoundException::new);
        if (!jwtUtil.isTokenValid(refreshToken, user)) return authResponse;

        var claims = new HashMap<String, Object>();
        claims.put("fullName", user.getFullName());
        claims.put("userId", user.getId());
        String newAccessToken = jwtUtil.generateToken(claims, user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .message("token was refreshed")
                .userId(String.valueOf(user.getId()))
                .build();
    }

    public void resendActivationToken(String email) throws MessagingException {
        User foundUser = userRepository
                .findUserEntityByEmail(email).orElseThrow(UserNotFoundException::new);
        if (foundUser.isEnabled())
            throw new ActivationTokenException("invalid request: account has already been activated");
        sendValidationEmail(foundUser);
    }

    private User getSignedUser() {
        log.info("current user name in Spring SecurityContext {}", SecurityContextHolder.getContext().getAuthentication().getName());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    private void setOnlineStatus(StatusType type) {
        // TODO: consider user settings after implementation
        User user = getSignedUser();
        userRepository.save(user);
    }

    private void setOnlineStatus(String email, StatusType type) {
        // TODO: consider user settings after implementation
        User user = userRepository.findUserEntityByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        userRepository.save(user);
    }

    public void requestUserRegistration(RegistrationRequest request) {
        User savedUser = register(request);
        UserRepresentation user = new UserRepresentation();
        BeanUtils.copyProperties(savedUser, user);
        user.setUserId(savedUser.getId());
        log.info("calling userCreation producer with user as event body: {}", user);
        try {
            userProducer.userCreation(UserCreationEvent.builder().eventBody(user).build());
        } catch (Exception e) {
            log.info("failed to produce userCreation event due to: {}, rolling back user", e.getMessage());
            userRepository.deleteById(savedUser.getId());
        }
    }

    public void requestUserDeletionById(Authentication auth, String userId) {
        try {
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(RoleType.ADMIN.toString())))
                throw new UnauthorizedException("access denied, insufficient user credentials");
            userRepository.deleteById(userId);
            userProducer.userDeletion(UserDeletionEvent.builder().userId(userId).build());
            log.info("user with id {} was deleted successfully", userId);
        } catch (Exception e) {
            log.info("user deletion failed due to: {}", e.getMessage());
        }
    }


}
