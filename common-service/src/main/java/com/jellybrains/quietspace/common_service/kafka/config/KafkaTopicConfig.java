package com.jellybrains.quietspace.common_service.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topics.chat}")
    private String chatTopic;

    @Value("${kafka.topics.notification}")
    private String notificationTopic;

    @Bean
    public NewTopic chatTopic() {
        return new NewTopic(chatTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic notificationTopic() {
        return new NewTopic(notificationTopic, 1, (short) 1);
    }

}
