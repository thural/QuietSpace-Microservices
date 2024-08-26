package com.jellybrains.quietspace_backend_ms.reaction_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jellybrains.quietspace.common_service"})
public class ReactionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactionServiceApplication.class, args);
    }

}
