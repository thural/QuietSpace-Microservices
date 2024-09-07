package com.jellybrains.quietspace.common_service.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class KafkaNotificationTopicConfig {

    @Value("${kafka.topics.notification}")
    private String notificationTopic;

    @Bean
    public NewTopic notificationTopic() {
        return new NewTopic(notificationTopic, 1, (short) 1);
    }

}