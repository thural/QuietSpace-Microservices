package com.jellybrains.quietspace.auth_service.rabbitmq.saga;

import com.jellybrains.quietspace.auth_service.entity.User;
import com.jellybrains.quietspace.auth_service.exception.UserNotFoundException;
import com.jellybrains.quietspace.auth_service.repository.UserRepository;
import com.jellybrains.quietspace.auth_service.service.impls.AuthService;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileCreationEvent;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileCreationEventFailed;
import com.jellybrains.quietspace.common_service.message.kafka.user.UserCreationEventFailed;
import com.jellybrains.quietspace.common_service.rabbitmq.producer.UserProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserCreationStep implements SagaStep<ProfileCreationEvent, ProfileCreationEventFailed> {

    private final AuthService authService;
    private final UserProducer userProducer;
    private final UserRepository userRepository;


    @RabbitListener(queues = "#{'${rabbitmq.queue.profile.creation}'}")
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

    @RabbitListener(queues = "#{'${rabbitmq.queue.profile.creation-failed}'}")
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