package com.jellybrains.quietspace.feed_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.jellybrains.quietspace.common_service.webclient",
        "com.jellybrains.quietspace.common_service.security",
        "com.jellybrains.quietspace.common_service.exception",
        "com.jellybrains.quietspace.common_service.kafka.config"
})
public class FeedServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedServiceApplication.class, args);
    }

}
