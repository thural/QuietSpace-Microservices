package com.jellybrains.quietspace.user_service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JwtFilter jwtFilter;

    //    @Bean // in case of not using Security filter
    public FilterRegistrationBean<JwtFilter> tokenFilter() {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtFilter);
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }

}

