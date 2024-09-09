package com.jellybrains.quietspace.user_service.kafka.saga;

import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileCreationEvent;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileCreationEventFailed;
import com.jellybrains.quietspace.common_service.message.kafka.user.UserCreationEvent;
import com.jellybrains.quietspace.common_service.message.kafka.user.UserCreationEventFailed;
import com.jellybrains.quietspace.user_service.entity.Profile;
import com.jellybrains.quietspace.user_service.kafka.producer.ProfileProducer;
import com.jellybrains.quietspace.user_service.repository.ProfileRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j
public class ProfileCreationStep implements SagaStep<UserCreationEvent, UserCreationEventFailed> {

    private final ProfileProducer profileProducer;
    private final ProfileRepository profileRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(
            noRollbackFor = {Throwable.class},
            propagation = Propagation.REQUIRES_NEW,
            isolation = Isolation.REPEATABLE_READ
    )
    @KafkaListener(topics = "#{'${kafka.topics.user.creation}'}")
    public void process(UserCreationEvent event) {
        try {
            log.info("event body at profile creation step: {}", event.getEventBody());
            var profile = new Profile();
            BeanUtils.copyProperties(event.getEventBody(), profile);
            Profile savedProfile = profileRepository.save(profile);
            log.info("saved profile id: {}", savedProfile);
            profileProducer.profileCreation(ProfileCreationEvent.builder()
                    .userId(profile.getUserId()).build());
        } catch (Exception e) {
            log.info("profile creation step was failed: {}", e.getMessage());
            var failEvent = ProfileCreationEventFailed.builder()
                    .userId(event.getEventBody().getUserId()).build();
            profileProducer.profileCreationFailed(failEvent);
            log.info("produced profileCreationFailed event: {}", failEvent);
        }
    }

    @Override
    @Transactional
    @KafkaListener(topics = "#{'${kafka.topics.user.creation-failed}'}")
    public void rollback(UserCreationEventFailed event) {
        profileProducer.profileCreationFailed(ProfileCreationEventFailed
                .builder()
                .userId(event.getUserId())
                .build());
        try {
            profileRepository.deleteByUserId(event.getUserId());
        } catch (Exception e) {
            log.info("profile creation rollback step was failed: {}", e.getMessage());
        }
    }


}
