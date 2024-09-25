package com.jellybrains.quietspace.user_service.rabbitmq.saga;

import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileDeletionEvent;
import com.jellybrains.quietspace.common_service.message.kafka.user.UserDeletionEvent;
import com.jellybrains.quietspace.common_service.message.kafka.user.UserDeletionFailedEvent;
import com.jellybrains.quietspace.common_service.rabbitmq.producer.ProfileProducer;
import com.jellybrains.quietspace.user_service.entity.Profile;
import com.jellybrains.quietspace.user_service.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileDeletionStep implements SagaStep<UserDeletionEvent, UserDeletionFailedEvent> {

    public static final String USER_PROFILE = "user_profile";
    private final ProfileProducer profileProducer;
    private final ProfileRepository profileRepository;
    private final RedisTemplate<String, Profile> redisTemplate;


    @Override
    @RabbitListener(queues = "#{'${rabbitmq.queue.user.deletion}'}")
    public void process(UserDeletionEvent event) {
        log.info("processing profile deletion step by userId: {}", event.getUserId());
        Profile foundProfile = profileRepository.findByUserId(event.getUserId())
                .orElseThrow(EntityNotFoundException::new);
        try {
            redisTemplate.opsForValue().set(USER_PROFILE, foundProfile);
            profileRepository.deleteByUserId(event.getUserId());
            profileProducer.profileDeleted(ProfileDeletionEvent.builder().userId(event.getUserId()).build());
        } catch (Exception e) {
            log.info("profile deletion step was failed: {}", e.getMessage());
        }
    }

    @Override
    @RabbitListener(queues = "#{'${rabbitmq.queue.user.deletion-failed}'}")
    public void rollback(UserDeletionFailedEvent event) {
        log.info("rolling back deleted profile on userDeletionFailedEvent: {}", event);
        Profile cachedProfile = redisTemplate.opsForValue().get(USER_PROFILE);
        if (Objects.isNull(cachedProfile)) throw new RuntimeException("cached profile not found");
        try {
            profileRepository.save(cachedProfile);
            redisTemplate.delete(USER_PROFILE);
        } catch (Exception e) {
            log.info("profile deletion rollback step was failed: {}", e.getMessage());
        }
    }


}
