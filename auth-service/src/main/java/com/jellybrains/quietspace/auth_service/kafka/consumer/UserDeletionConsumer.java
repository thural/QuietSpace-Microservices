package com.jellybrains.quietspace.auth_service.kafka.consumer;

import com.jellybrains.quietspace.auth_service.repository.UserRepository;
import com.jellybrains.quietspace.common_service.enums.EventType;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileDeletionFailedEvent;
import com.jellybrains.quietspace.common_service.message.kafka.user.UserProfileEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDeletionConsumer {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, ProfileDeletionFailedEvent> deleteUserKafkaTemplate;

    @Value("${kafka.topics.user-profile}")
    private String userProfileTopic;

    @KafkaListener(topics = "#{'${kafka.topics.user-profile}'}")
    public void deleteUserById(UserProfileEvent event) {
        if (!event.getType().equals(EventType.PROFILE_DELETED)) return;
        String userId = event.getUserId();

        try {
            userRepository.deleteById(userId);
            // TODO: send event to public websocket subscriber
            log.info("user deletion successful for userId: {}", userId);
        } catch (Exception e) {
            log.info("user deletion failed for userId: {} due to: {}", event.getUserId(), e.getMessage());
            deleteUserKafkaTemplate.send(userProfileTopic,
                    ProfileDeletionFailedEvent.builder().userId(userId).build());
        }
    }
}