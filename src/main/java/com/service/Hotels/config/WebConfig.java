package com.service.Hotels.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean<LowerCaseUrlFilter> lowercaseUrlFilter() {
        FilterRegistrationBean<LowerCaseUrlFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LowerCaseUrlFilter());
        registrationBean.addUrlPatterns("/*"); // Aplicar el filtro a todas las URL
        return registrationBean;
    }
}

