package com.jellybrains.quietspace.chat_service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AppConfig {

    @Value("${spring.application.urls.frontend}")
    private String FRONTEND_URL;


    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                HttpHeaders.CACHE_CONTROL,
                HttpHeaders.ORIGIN,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT,
                HttpHeaders.AUTHORIZATION
        ));
        config.setAllowedMethods(Stream.of(
                HttpMethod.GET,
                HttpMethod.POST,
                HttpMethod.DELETE,
                HttpMethod.PUT,
                HttpMethod.PATCH
        ).map(HttpMethod::toString).toList());
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}