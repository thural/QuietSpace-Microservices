package com.jellybrains.quietspace.auth_service.kafka.consumer;

import com.jellybrains.quietspace.auth_service.kafka.producer.UserProducer;
import com.jellybrains.quietspace.auth_service.repository.UserRepository;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileDeletionEvent;
import com.jellybrains.quietspace.common_service.message.kafka.user.UserDeletionEvent;
import com.jellybrains.quietspace.common_service.message.kafka.user.UserDeletionFailedEvent;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProfileDeletionConsumer {

    private final UserProducer userProducer;
    private final UserRepository userRepository;

    @KafkaListener(topics = "#{'${kafka.topics.profile.deletion}'}")
    public void deleteProfileUser(ProfileDeletionEvent event) {
        try {
            log.info("deleting user on profileDeletionEvent: {}", event);
            if (userRepository.existsById(event.getUserId()))
                throw new EntityNotFoundException("requested user not found");
            userRepository.deleteById(event.getUserId());
            userProducer.userDeletion(UserDeletionEvent.builder().userId(event.getUserId()).build());
            // TODO: send event to websocket subscriber
            log.info("user deletion successful for userId: {}", event.getUserId());
        } catch (Exception e) {
            log.info("user deletion failed for userId: {} due to: {}", event.getUserId(), e.getMessage());
            userProducer.userDeletionFailed(UserDeletionFailedEvent.builder().userId(event.getUserId()).build());
        }
    }

}