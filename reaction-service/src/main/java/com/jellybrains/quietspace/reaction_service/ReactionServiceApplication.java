package com.jellybrains.quietspace.reaction_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.jellybrains.quietspace.common_service.kafka.config",
        "com.jellybrains.quietspace.common_service.kafka.producer",
        "com.jellybrains.quietspace.common_service.webclient",
        "com.jellybrains.quietspace.common_service.security",
        "com.jellybrains.quietspace.common_service.exception"
})
public class ReactionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactionServiceApplication.class, args);
    }

}
