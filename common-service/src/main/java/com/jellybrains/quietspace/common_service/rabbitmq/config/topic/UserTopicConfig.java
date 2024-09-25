package com.jellybrains.quietspace.common_service.rabbitmq.config.topic;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserTopicConfig {

    @Value("${rabbitmq.topic.user}")
    String userTopicExchange;

    @Value("${rabbitmq.queue.user.creation}")
    String userCreation;

    @Value("${rabbitmq.queue.user.creation-failed}")
    String userCreationFailed;

    @Value("${rabbitmq.queue.user.deletion}")
    String userDeletion;

    @Value("${rabbitmq.queue.user.deletion-failed}")
    String userDeletionFailed;

    @Value("${rabbitmq.queue.user.update}")
    String userUpdate;

    @Value("${rabbitmq.queue.user.update-failed}")
    String userUpdateFailed;


    @Bean
    public TopicExchange userTopicExchange() {
        return new TopicExchange(userTopicExchange);
    }

    @Bean
    public Queue userCreationQueue() {
        return new Queue(userCreation, true);
    }

    @Bean
    public Queue userCreationFailedQueue() {
        return new Queue(userCreationFailed, true);
    }

    @Bean
    public Queue userDeletionQueue() {
        return new Queue(userDeletion, true);
    }

    @Bean
    public Queue userDeletionFailedQueue() {
        return new Queue(userDeletionFailed, true);
    }

    @Bean
    public Queue userUpdateQueue() {
        return new Queue(userUpdate, true);
    }

    @Bean
    public Queue userUpdateFailedQueue() {
        return new Queue(userUpdateFailed, true);
    }


    @Bean
    public Binding userCreationBinding() {
        return BindingBuilder.bind(userCreationQueue())
                .to(userTopicExchange())
                .with(userCreation);
    }

    @Bean
    public Binding userCreationFailedBinding() {
        return BindingBuilder.bind(userCreationFailedQueue())
                .to(userTopicExchange())
                .with(userCreationFailed);
    }

    @Bean
    public Binding userDeletionBinding() {
        return BindingBuilder.bind(userDeletionQueue())
                .to(userTopicExchange())
                .with(userDeletion);
    }

    @Bean
    public Binding userDeletionFailedBinding() {
        return BindingBuilder.bind(userDeletionFailedQueue())
                .to(userTopicExchange())
                .with(userDeletionFailed);
    }

    @Bean
    public Binding userUpdateBinding() {
        return BindingBuilder.bind(userUpdateQueue())
                .to(userTopicExchange())
                .with(userUpdate);
    }

    @Bean
    public Binding userUpdateFailedBinding() {
        return BindingBuilder.bind(userUpdateFailedQueue())
                .to(userTopicExchange())
                .with(userUpdateFailed);
    }
}
