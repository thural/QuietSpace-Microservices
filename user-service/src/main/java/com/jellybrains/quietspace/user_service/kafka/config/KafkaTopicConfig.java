package com.jellybrains.quietspace.user_service.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topics.profile}")
    private String profileTopic;

    @Value("${kafka.topics.user}")
    private String userTopic;

    @Bean
    NewTopic profileTopic() {
        return new NewTopic(profileTopic, 1, (short) 1);
    }

    @Bean
    NewTopic userTopic() {
        return new NewTopic(userTopic, 1, (short) 1);
    }

}
