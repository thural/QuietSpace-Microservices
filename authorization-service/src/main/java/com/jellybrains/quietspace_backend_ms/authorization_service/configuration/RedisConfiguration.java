package com.jellybrains.quietspace_backend_ms.authorization_service.configuration;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching
public class RedisConfiguration {

    @Bean
    public ReactiveRedisTemplate<String, UserRepresentation> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        return new ReactiveRedisTemplate<String, UserRepresentation>(
                reactiveRedisConnectionFactory,
                RedisSerializationContext.fromSerializer(new Jackson2JsonRedisSerializer(UserRepresentation.class))
        );
    }
}
