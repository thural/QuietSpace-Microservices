package com.jellybrains.quietspace.chat_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    NewTopic chatTopic() {
        return new NewTopic("chat-topic", 1, (short) 1);
    }

}
