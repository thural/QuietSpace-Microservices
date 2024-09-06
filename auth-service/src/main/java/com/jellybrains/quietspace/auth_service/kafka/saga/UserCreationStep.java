package com.jellybrains.quietspace.auth_service.kafka.saga;

import com.jellybrains.quietspace.auth_service.entity.User;
import com.jellybrains.quietspace.auth_service.exception.UserNotFoundException;
import com.jellybrains.quietspace.auth_service.kafka.producer.UserProducer;
import com.jellybrains.quietspace.auth_service.repository.UserRepository;
import com.jellybrains.quietspace.auth_service.service.impls.AuthService;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileCreationEvent;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileCreationEventFailed;
import com.jellybrains.quietspace.common_service.message.kafka.user.UserCreationEventFailed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserCreationStep implements SagaStep<ProfileCreationEvent, ProfileCreationEventFailed> {

    private final AuthService authService;
    private final UserProducer userProducer;
    private final UserRepository userRepository;


    @KafkaListener(topics = "#{'${kafka.topics.profile.creation}'}")
    public void process(ProfileCreationEvent event) {
        try {
            log.info("userId at user creation step: {}", event.getUserId());
            User savedUser = userRepository.findById(event.getUserId())
                    .orElseThrow(UserNotFoundException::new);
            authService.sendValidationEmail(savedUser);
            log.info("user registration successful for userId: {}", event.getUserId());
        } catch (Exception e) {
            log.info("user registration failed for userId: {} due to: {}", event.getUserId(), e.getMessage());
            userProducer.userCreationFailed(UserCreationEventFailed.builder().userId(event.getUserId()).build());
        }
    }

    @KafkaListener(topics = "#{'${kafka.topics.profile.creation-failed}'}")
    public void rollback(ProfileCreationEventFailed event) {
        try {
            log.info("rolling back user registration on profileCreationEventFailed: {}", event);
            userRepository.deleteById(event.getUserId());
            // TODO: send event to public websocket subscriber
            log.info("user registration rollback successful for userId: {}", event.getUserId());
        } catch (Exception e) {
            log.info("user registration rollback failed for userId: {} due to: {}", event.getUserId(), e.getMessage());
        }
    }


}