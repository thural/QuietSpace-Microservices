package com.jellybrains.quietspace.user_service.saga;

import com.jellybrains.quietspace.common_service.enums.EventType;
import com.jellybrains.quietspace.common_service.message.ProfileDeletionEvent;
import com.jellybrains.quietspace.common_service.message.ProfileDeletionFailedEvent;
import com.jellybrains.quietspace.common_service.message.UserProfileEvent;
import com.jellybrains.quietspace.user_service.entity.Profile;
import com.jellybrains.quietspace.user_service.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
@Slf4j
public class ProfileDeletionStep implements SagaStep<ProfileDeletionEvent, ProfileDeletionFailedEvent> {

    public static final String USER_PROFILE = "user_profile";
    private final ProfileRepository profileRepository;
    private final RedisTemplate<String, Profile> redisTemplate;
    private final KafkaTemplate<String, UserProfileEvent> kafkaTemplate;

    @Value("${kafka.topics.user-profile}")
    private String userProfileTopic;

    @Override
    @KafkaListener(topics = "#{'${kafka.topics.user-profile}'}")
    public void process(ProfileDeletionEvent event) {
        if (!event.getType().equals(EventType.PROFILE_DELETION_REQUEST)) return;
        String userId = event.getUserId();

        Profile foundProfile = profileRepository.findByUserId(userId)
                .orElseThrow(EntityNotFoundException::new);

        try {
            redisTemplate.opsForValue().set(USER_PROFILE, foundProfile);
            profileRepository.deleteByUserId(userId);

            kafkaTemplate.send(userProfileTopic,
                    UserProfileEvent.builder()
                            .type(EventType.PROFILE_DELETED)
                            .userId(userId)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("profile deletion step was failed");
        }
    }

    @Override
    @KafkaListener(topics = "#{'${kafka.topics.user-profile}'}")
    public void rollback(ProfileDeletionFailedEvent event) {
        if (event.getType() != EventType.PROFILE_DELETION_FAILED) return;
        Profile cachedProfile = redisTemplate.opsForValue().get(USER_PROFILE);
        if (Objects.isNull(cachedProfile)) throw new RuntimeException("cached profile not found");

        try {
            profileRepository.save(cachedProfile);
            redisTemplate.delete(USER_PROFILE);
        } catch (Exception e) {
            throw new RuntimeException("profile deletion rollback step was failed");
        }
    }
}
