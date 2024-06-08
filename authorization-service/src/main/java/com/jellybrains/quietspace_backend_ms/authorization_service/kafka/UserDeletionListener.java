package com.jellybrains.quietspace_backend_ms.authorization_service.kafka;

import com.jellybrains.quietspace_backend_ms.authorization_service.event.deletion.DeleteUserEventFailedEvent;
import com.jellybrains.quietspace_backend_ms.authorization_service.event.deletion.DeletedUserProfileEvent;
import com.jellybrains.quietspace_backend_ms.authorization_service.service.KeycloakService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDeletionListener {

    private final KeycloakService keycloakService;
    private final KafkaTemplate<String, DeleteUserEventFailedEvent> deleteUserKafkaTemplate;

    @Value("${kafka.topics.user-profile-deletion-failed}")
    private String userProfileDeletionFailedTopic;

    @KafkaListener(topics = "#{'${kafka.topics.user-profile-deleted}'}")
    public void deleteUserById(DeletedUserProfileEvent event) {

        String userId = event.getUserId().toString();
        try (Response delete = keycloakService.getUsersResource().delete(userId)) {

            if (delete.getStatus() == 204) {
                log.info("User deletion successful for userId: {}", userId);
            } else {
                throw new RuntimeException("Deletion failed. Response Status is " + delete.getStatus());
            }
        } catch (Exception e) {
            deleteUserKafkaTemplate.send(userProfileDeletionFailedTopic, null);
            log.info("User deletion failed for userId: {} due to: {}", event, e.getMessage());
        }
    }
}
