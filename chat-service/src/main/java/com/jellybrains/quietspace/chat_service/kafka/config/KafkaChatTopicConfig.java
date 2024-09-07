package com.jellybrains.quietspace.chat_service.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaChatTopicConfig {

    @Value("${kafka.topics.chat.event.send}")
    private String chatEventSendTopic;

    @Value("${kafka.topics.chat.event.delete}")
    private String chatEventDeleteTopic;

    @Value("${kafka.topics.chat.event.seen}")
    private String chatEventSeenTopic;

    @Value("${kafka.topics.chat.event.leave}")
    private String chatEventLeaveTopic;

    @Value("${kafka.topics.chat.event.join}")
    private String chatEventJoinTopic;


    @Bean
    public NewTopic chatEventSendTopic() {
        return new NewTopic(chatEventSendTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic chatEventDeleteTopic() {
        return new NewTopic(chatEventDeleteTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic chatEventSeenTopic() {
        return new NewTopic(chatEventSeenTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic chatEventLeaveTopic() {
        return new NewTopic(chatEventLeaveTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic chatEventJoinTopic() {
        return new NewTopic(chatEventJoinTopic, 1, (short) 1);
    }

}
