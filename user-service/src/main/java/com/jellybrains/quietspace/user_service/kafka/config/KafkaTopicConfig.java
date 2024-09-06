package com.jellybrains.quietspace.user_service.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topics.profile.creation}")
    private String profileCreationTopic;

    @Value("${kafka.topics.profile.creation-failed}")
    private String profileCreationFailedTopic;

    @Value("${kafka.topics.profile.deletion}")
    private String profileDeletionTopic;

    @Value("${kafka.topics.profile.deletion-failed}")
    private String profileDeletionFailedTopic;

    @Bean
    public NewTopic profileCreationTopic() {
        return new NewTopic(profileCreationTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic profileCreationFailedTopic() {
        return new NewTopic(profileCreationFailedTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic profileDeletionTopic() {
        return new NewTopic(profileDeletionFailedTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic profileDeletionFailedTopic() {
        return new NewTopic(profileDeletionFailedTopic, 1, (short) 1);
    }

}
