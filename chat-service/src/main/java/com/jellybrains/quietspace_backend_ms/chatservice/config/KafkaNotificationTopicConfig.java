package com.jellybrains.quietspace_backend_ms.chatservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaNotificationTopicConfig {

    @Bean
    public NewTopic notificationTopic() {
        return new NewTopic("notification-topic", 1, (short) 1);
    }

}
