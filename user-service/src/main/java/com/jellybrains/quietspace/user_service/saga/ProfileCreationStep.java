package com.jellybrains.quietspace.user_service.saga;

import com.jellybrains.quietspace.common_service.enums.EventType;
import com.jellybrains.quietspace.common_service.message.*;
import com.jellybrains.quietspace.user_service.entity.Profile;
import com.jellybrains.quietspace.user_service.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ProfileCreationStep implements SagaStep<UserCreationEvent, UserCreationEventFailed> {

    private final ProfileRepository profileRepository;
    private final KafkaTemplate<String, UserProfileEvent> kafkaTemplate;

    @Value("${kafka.topics.user-profile}")
    private String userProfileTopic;

    @Override
    @KafkaListener(topics = "#{'${kafka.topics.user-profile}'}")
    public void process(UserCreationEvent event) {
        if (!event.getType().equals(EventType.USER_CREATED)) return;
        String userId = event.getUserId();

        try {
            Profile profile = new Profile();
            BeanUtils.copyProperties(event.getEventBody(), profile);
            profileRepository.save(profile);

            kafkaTemplate.send(userProfileTopic,
                    ProfileCreationEvent.builder()
                            .type(EventType.PROFILE_CREATED)
                            .userId(userId)
                            .build()
            );
        } catch (Exception e) {
            kafkaTemplate.send(userProfileTopic,
                    ProfileCreationEventFailed.builder()
                            .userId(userId)
                            .build()
            );
            throw new RuntimeException("profile creation step was failed");
        }
    }

    @Override
    @KafkaListener(topics = "#{'${kafka.topics.user-profile}'}")
    public void rollback(UserCreationEventFailed event) {
        if (event.getType() != EventType.USER_CREATION_FAILED) return;

        kafkaTemplate.send(userProfileTopic,
                ProfileCreationEventFailed.builder()
                        .userId(event.getUserId())
                        .build()
        );

        try {
            profileRepository.deleteByUserId(event.getUserId());

        } catch (Exception e) {
            throw new RuntimeException("profile creation rollback step was failed");
        }
    }
}
