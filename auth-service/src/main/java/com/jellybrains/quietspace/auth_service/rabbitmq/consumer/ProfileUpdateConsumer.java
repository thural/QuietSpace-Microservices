package com.jellybrains.quietspace.auth_service.rabbitmq.consumer;

import com.jellybrains.quietspace.auth_service.entity.User;
import com.jellybrains.quietspace.auth_service.repository.UserRepository;
import com.jellybrains.quietspace.common_service.enums.EventType;
import com.jellybrains.quietspace.common_service.message.kafka.profile.ProfileUpdateEvent;
import com.jellybrains.quietspace.common_service.message.kafka.user.UserUpdateFailedEvent;
import com.jellybrains.quietspace.common_service.rabbitmq.producer.UserProducer;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProfileUpdateConsumer {

    private final UserProducer userProducer;
    private final UserRepository userRepository;

    @RabbitListener(queues = "#{'${rabbitmq.queue.profile.update}'}")
    public void updateProfileUser(ProfileUpdateEvent event) {
        if (!event.getType().equals(EventType.PROFILE_UPDATED_EVENT)) return;
        String userId = event.getEventBody().getUserId();
        try {
            log.info("updating user on profileUpdateEvent: {}", event);
            User requestedUser = userRepository.findById(userId)
                    .orElseThrow(EntityNotFoundException::new);
            BeanUtils.copyProperties(event.getEventBody(), requestedUser);
        } catch (Exception e) {
            log.info("user update failed for userId: {} due to: {}", userId, e.getMessage());
            userProducer.userUpdateFailed(UserUpdateFailedEvent.builder().userId(userId).build());
        }
    }


}