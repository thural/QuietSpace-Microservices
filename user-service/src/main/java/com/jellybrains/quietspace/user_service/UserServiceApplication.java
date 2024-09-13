package com.jellybrains.quietspace.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.jellybrains.quietspace.common_service.webclient",
        "com.jellybrains.quietspace.common_service.security",
        "com.jellybrains.quietspace.common_service.exception",
        "com.jellybrains.quietspace.common_service.kafka.config",
        "com.jellybrains.quietspace.common_service.kafka.producer",
        "com.jellybrains.quietspace.user_service"
})
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
