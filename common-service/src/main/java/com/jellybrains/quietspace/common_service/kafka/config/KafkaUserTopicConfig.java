package com.jellybrains.quietspace.common_service.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaUserTopicConfig {

    @Value("${kafka.topics.user.creation}")
    private String userCreationTopic;

    @Value("${kafka.topics.user.creation-failed}")
    private String userCreationFailedTopic;

    @Value("${kafka.topics.user.update-failed}")
    private String userUpdateFailedTopic;

    @Value("${kafka.topics.user.deletion}")
    private String userDeletionTopic;

    @Value("${kafka.topics.user.deletion-failed}")
    private String userDeletionFailedTopic;

    @Bean
    public NewTopic userCreationTopic() {
        return new NewTopic(userCreationTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic userCreationFailedTopic() {
        return new NewTopic(userCreationFailedTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic userDeletionTopic() {
        return new NewTopic(userDeletionTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic userDeletionFailedTopic() {
        return new NewTopic(userDeletionFailedTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic userUpdateFailedTopic() {
        return new NewTopic(userUpdateFailedTopic, 1, (short) 1);
    }

}
