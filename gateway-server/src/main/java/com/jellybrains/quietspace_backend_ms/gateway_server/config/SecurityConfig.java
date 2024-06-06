package com.jellybrains.quietspace_backend_ms.gateway_server.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity httpSecurity,
                                                            Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthConverter) {
        httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/eureka/**", "/auth/**", "/api/v1/user/**").permitAll()
                                .pathMatchers("/api/v1/dummy/admin/**").hasRole("ADMIN")
                                .pathMatchers("/api/v1/dummy/**").permitAll()
                                .anyExchange().authenticated()
                        )
//                .oauth2Login(Customizer.withDefaults())
                .oauth2ResourceServer(it -> it.jwt(j -> j.jwtAuthenticationConverter(jwtAuthConverter))
                );
        return httpSecurity.build();
    }
}
