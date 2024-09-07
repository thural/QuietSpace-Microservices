package com.jellybrains.quietspace.common_service.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaChatTopicConfig {

    @Value("${kafka.topics.chat.request.send}")
    private String chatRequestSendTopic;

    @Value("${kafka.topics.chat.request.delete}")
    private String chatRequestDeleteTopic;

    @Value("${kafka.topics.chat.request.seen}")
    private String chatRequestSeenTopic;

    @Value("${kafka.topics.chat.request.leave}")
    private String chatRequestLeaveTopic;

    @Value("${kafka.topics.chat.request.join}")
    private String chatRequestJoinTopic;


    @Bean
    public NewTopic chatRequestSendTopic() {
        return new NewTopic(chatRequestSendTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic chatRequestDeleteTopic() {
        return new NewTopic(chatRequestDeleteTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic chatRequestSeenTopic() {
        return new NewTopic(chatRequestSeenTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic chatRequestLeaveTopic() {
        return new NewTopic(chatRequestLeaveTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic chatRequestJoinTopic() {
        return new NewTopic(chatRequestJoinTopic, 1, (short) 1);
    }

}
