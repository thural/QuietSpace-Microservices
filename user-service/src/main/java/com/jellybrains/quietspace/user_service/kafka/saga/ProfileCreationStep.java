package com.jellybrains.quietspace.user_service.kafka.saga;

import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileCreationEvent;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileCreationEventFailed;
import com.jellybrains.quietspace.common_service.message.kafka.user.UserCreationEvent;
import com.jellybrains.quietspace.common_service.message.kafka.user.UserCreationEventFailed;
import com.jellybrains.quietspace.user_service.entity.Profile;
import com.jellybrains.quietspace.user_service.kafka.producer.ProfileProducer;
import com.jellybrains.quietspace.user_service.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ProfileCreationStep implements SagaStep<UserCreationEvent, UserCreationEventFailed> {

    private final ProfileProducer profileProducer;
    private final ProfileRepository profileRepository;

    @Override
    @KafkaListener(topics = "#{'${kafka.topics.user.creation}'}")
    public void process(UserCreationEvent event) {
        try {
            log.info("event body at profile creation step: {}", event.getEventBody());
            Profile profile = new Profile();
            BeanUtils.copyProperties(event.getEventBody(), profile);
            profileRepository.save(profile);
            profileProducer.profileCreation(ProfileCreationEvent.builder().userId(event.getUserId()).build());
        } catch (Exception e) {
            profileProducer.profileCreationFailed(ProfileCreationEventFailed
                    .builder()
                    .userId(event.getUserId())
                    .build());
            throw new RuntimeException("profile creation step was failed");
        }
    }

    @Override
    @KafkaListener(topics = "#{'${kafka.topics.user.creation-failed}'}")
    public void rollback(UserCreationEventFailed event) {
        profileProducer.profileCreationFailed(ProfileCreationEventFailed
                .builder()
                .userId(event.getUserId())
                .build());
        try {
            profileRepository.deleteByUserId(event.getUserId());
        } catch (Exception e) {
            throw new RuntimeException("profile creation rollback step was failed");
        }
    }


}
