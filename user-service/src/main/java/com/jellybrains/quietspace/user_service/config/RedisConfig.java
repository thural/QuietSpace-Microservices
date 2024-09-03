package com.jellybrains.quietspace.user_service.config;

import com.jellybrains.quietspace.user_service.entity.Profile;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, Profile> redisTemplate(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        return new ReactiveRedisTemplate<String, Profile>(
                reactiveRedisConnectionFactory,
                RedisSerializationContext.fromSerializer(new Jackson2JsonRedisSerializer(Profile.class))
        );
    }
}
