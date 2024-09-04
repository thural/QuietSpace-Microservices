package com.jellybrains.quietspace.user_service.config;

import com.jellybrains.quietspace.user_service.entity.Profile;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Profile> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Profile> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

}

