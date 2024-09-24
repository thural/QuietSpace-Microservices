package com.jellybrains.quietspace.common_service.rabbitmq.config.topic;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class ProfileTopicConfig {

    @Value("${rabbitmq.topic.profile}")
    String profileTopicExchange;

    @Value("$[rabbitmq.queue.profile.creation}")
    String profileCreation;

    @Value("$[rabbitmq.queue.profile.creation-failed}")
    String profileCreationFailed;

    @Value("$[rabbitmq.queue.profile.deletion}")
    String profileDeletion;

    @Value("$[rabbitmq.queue.profile.deletion-failed}")
    String profileDeletionFailed;

    @Value("$[rabbitmq.queue.profile.update}")
    String profileUpdate;

    @Value("$[rabbitmq.queue.profile.update-failed}")
    String profileUpdateFailed;


    @Bean
    public TopicExchange profileTopicExchange() {
        return new TopicExchange(profileTopicExchange);
    }

    @Bean
    public Queue profileCreationQueue() {
        return new Queue(profileCreation, true);
    }

    @Bean
    public Queue profileCreationFailedQueue() {
        return new Queue(profileCreationFailed, true);
    }

    @Bean
    public Queue profileDeletionQueue() {
        return new Queue(profileDeletion, true);
    }

    @Bean
    public Queue profileDeletionFailedQueue() {
        return new Queue(profileDeletionFailed, true);
    }

    @Bean
    public Queue profileUpdateQueue() {
        return new Queue(profileUpdate, true);
    }

    @Bean
    public Queue profileUpdateFailedQueue() {
        return new Queue(profileUpdateFailed, true);
    }


    @Bean
    public Binding profileCreationBinding() {
        return BindingBuilder.bind(profileCreationQueue())
                .to(profileTopicExchange())
                .with(profileCreation);
    }

    @Bean
    public Binding profileCreationFailedBinding() {
        return BindingBuilder.bind(profileCreationFailedQueue())
                .to(profileTopicExchange())
                .with(profileCreationFailed);
    }

    @Bean
    public Binding profileDeletionBinding() {
        return BindingBuilder.bind(profileDeletionQueue())
                .to(profileTopicExchange())
                .with(profileDeletionFailed);
    }

    @Bean
    public Binding profileDeletionFailedBinding() {
        return BindingBuilder.bind(profileDeletionFailedQueue())
                .to(profileTopicExchange())
                .with(profileDeletionFailed);
    }

    @Bean
    public Binding profileUpdateBinding() {
        return BindingBuilder.bind(profileUpdateQueue())
                .to(profileTopicExchange())
                .with(profileUpdate);
    }

    @Bean
    public Binding profileUpdateFailedBinding() {
        return BindingBuilder.bind(profileUpdateFailedQueue())
                .to(profileTopicExchange())
                .with(profileUpdateFailed);
    }
}
