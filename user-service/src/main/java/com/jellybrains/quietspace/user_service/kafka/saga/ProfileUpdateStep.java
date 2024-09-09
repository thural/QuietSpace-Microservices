package com.jellybrains.quietspace.user_service.kafka.saga;

import com.jellybrains.quietspace.common_service.enums.EventType;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileUpdateEvent;
import com.jellybrains.quietspace.common_service.message.kafka.user.UserUpdateFailedEvent;
import com.jellybrains.quietspace.common_service.websocket.model.UserRepresentation;
import com.jellybrains.quietspace.user_service.entity.Profile;
import com.jellybrains.quietspace.user_service.kafka.producer.ProfileProducer;
import com.jellybrains.quietspace.user_service.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
@Slf4j
public class ProfileUpdateStep implements SagaStep<ProfileUpdateEvent, UserUpdateFailedEvent> {

    public static final String CACHED_PROFILE = "cached_profile";
    private final ProfileProducer profileProducer;
    private final ProfileRepository profileRepository;
    private final RedisTemplate<String, Profile> redisTemplate;


    @Override
    @KafkaListener(topics = "#{'${kafka.topics.profile.update}'}")
    public void process(ProfileUpdateEvent event) {
        if (!event.getType().equals(EventType.PROFILE_UPDATE_REQUEST_EVENT)) return;
        UserRepresentation user = event.getEventBody();
        log.info("processing profile update step by userId: {}", user.getUserId());
        Profile foundProfile = profileRepository.findByUserId(user.getUserId())
                .orElseThrow(EntityNotFoundException::new);
        try {
            redisTemplate.opsForValue().set(CACHED_PROFILE, foundProfile);
            BeanUtils.copyProperties(user, foundProfile);
            profileProducer.profileUpdate(ProfileUpdateEvent.builder()
                    .type(EventType.PROFILE_UPDATED_EVENT)
                    .eventBody(user).build());
        } catch (Exception e) {
            log.info("profile update step was failed: {}", e.getMessage());
        }
    }

    @Override
    @KafkaListener(topics = "#{'${kafka.topics.user.update-failed}'}")
    public void rollback(UserUpdateFailedEvent event) {
        log.info("rolling back profile update on userUpdateFailedEvent: {}", event);
        Profile cachedProfile = redisTemplate.opsForValue().get(CACHED_PROFILE);
        if (Objects.isNull(cachedProfile)) throw new RuntimeException("cached profile not found");
        try {
            profileRepository.save(cachedProfile);
            redisTemplate.delete(CACHED_PROFILE);
        } catch (Exception e) {
            log.info("profile update rollback step was failed: {}", e.getMessage());
        }
    }


}
