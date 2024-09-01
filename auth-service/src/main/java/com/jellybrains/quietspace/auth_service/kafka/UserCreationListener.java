package com.jellybrains.quietspace.auth_service.kafka;

import com.jellybrains.quietspace.auth_service.entity.User;
import com.jellybrains.quietspace.auth_service.exception.UserNotFoundException;
import com.jellybrains.quietspace.auth_service.repository.UserRepository;
import com.jellybrains.quietspace.auth_service.service.impls.AuthService;
import com.jellybrains.quietspace.common_service.enums.EventType;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileCreationEvent;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileCreationEventFailed;
import com.jellybrains.quietspace.common_service.message.kafka.user.UserCreationEventFailed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserCreationListener {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final KafkaTemplate<String, UserCreationEventFailed> createUserKafkaTemplate;

    @Value("${kafka.topics.user-profile}")
    private String userProfileTopic;

    @KafkaListener(topics = "#{'${kafka.topics.user-profile}'}")
    public void completeUserCreation(ProfileCreationEvent event) {
        if (!event.getType().equals(EventType.PROFILE_CREATED)) return;
        String userId = event.getUserId();

        try {
            User savedUser = userRepository.findById(event.getUserId())
                    .orElseThrow(UserNotFoundException::new);

            authService.sendValidationEmail(savedUser);
            log.info("user registration successful for userId: {}", userId);

        } catch (Exception e) {
            log.info("user registration failed for userId: {} due to: {}", event.getUserId(), e.getMessage());
            createUserKafkaTemplate.send(userProfileTopic,
                    UserCreationEventFailed.builder().userId(userId).build());
        }
    }

    @KafkaListener(topics = "#{'${kafka.topics.user-profile}'}")
    public void rollbackUserCreation(ProfileCreationEventFailed event) {
        if (!event.getType().equals(EventType.PROFILE_CREATION_FAILED)) return;
        String userId = event.getUserId();

        try {
            userRepository.deleteById(event.getUserId());
            // TODO: send event to public websocket subscriber
            log.info("user registration rollback successful for userId: {}", userId);

        } catch (Exception e) {
            log.info("user registration rollback failed for userId: {} due to: {}", event.getUserId(), e.getMessage());
        }
    }
}