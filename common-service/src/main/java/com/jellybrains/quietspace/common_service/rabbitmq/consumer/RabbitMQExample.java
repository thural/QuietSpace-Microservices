package com.jellybrains.quietspace.common_service.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQExample {

    @Value("${rabbitmq.queue.profile.creation}")
    String profileCreation;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend("myQueue", message);
        System.out.println("Message sent: " + message);
    }

    @RabbitListener(queues = "#{'${rabbitmq.queue.profile.creation}'}")
    public void receiveMessage(String message) {
        System.out.println("Message received: " + message);
    }
}
